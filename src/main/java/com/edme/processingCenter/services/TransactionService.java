package com.edme.processingCenter.services;

import com.edme.commondto.dto.TransactionExchangeDto;
import com.edme.processingCenter.dto.TransactionDto;
import com.edme.processingCenter.dto.TransactionExchangeIbDto;
import com.edme.processingCenter.dto.TransactionTypeDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.AccountMapper;
import com.edme.processingCenter.mappers.TransactionExchangeMapper;
import com.edme.processingCenter.mappers.TransactionMapper;
import com.edme.processingCenter.mappers.TransactionTypeMapper;
import com.edme.processingCenter.models.Transaction;
import com.edme.processingCenter.repositories.TransactionRepository;
import com.edme.processingCenter.services.feign.SalesPointClientService;
import com.edme.processingCenter.services.rabbitMQ.IssuingBankClientService;
import com.edme.processingCenter.utils.DelayForTestSwagger;
import feign.FeignException;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService implements AbstractService<Long, TransactionDto> {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionTypeMapper transactionTypeMapper;
    private final AccountMapper accountMapper;
    private final SalesPointClientService salesPointClientService;
    private final IssuingBankClientService issuingBankClientService;
    private final TransactionExchangeMapper transactionExchangeMapper;
    private final AccountService accountService;
    private final TransactionTypeService transactionTypeService;
    private final RabbitTemplate rabbitTemplate;
    private final Tracer tracer;
    private final CacheManager cacheManager;

    //Tracer tracer = GlobalOpenTelemetry.getTracer("processing-center");


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "allTransactionsCache")
    public List<TransactionDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "transactionByIdCache", key = "#id")
    public Optional<TransactionDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return transactionRepository.findById(aLong)
                .map(transactionMapper::toDto);
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public Optional<TransactionDto> save(TransactionDto dto) {
        Optional<Transaction> exist = transactionRepository.findById(dto.getId());
        if (exist.isPresent()) {
            log.info("Transaction already exists with id: {}", dto.getId());
            return Optional.empty();
        }
        dto.setId(null);
        Transaction saved = transactionRepository.saveAndFlush(transactionMapper.toEntity(dto));
        log.info("Saved transaction: {}", saved);
        return Optional.ofNullable(transactionMapper.toDto(saved));
    }


    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public Optional<TransactionExchangeDto> saveAndSendTransaction(TransactionDto transactionDto) {
        Span span = tracer.spanBuilder("saveAndSendTransaction").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("processing-center", "создание транзакции");
            transactionDto.setId(null); // Сбрасываем ID, чтобы создать новую
            // Устанавливаем текущую дату:
            transactionDto.setTransactionDate(LocalDate.now());
            Transaction savedTransaction = transactionRepository.save(transactionMapper.toEntity(transactionDto));
            log.info("Transaction saved locally with id: {}", savedTransaction.getId());

            updateAccountBalance(savedTransaction);

            TransactionExchangeDto exchangeDto = convertToExchangeDto(transactionDto);
            TransactionExchangeIbDto transactionExchangeIbDto = convertToTransactionExchangeIbDto(transactionDto);
            cacheManager.getCache("transactionByIdCache").put(savedTransaction.getId(), savedTransaction);

            SpanContext spanContext = span.getSpanContext();
            if (spanContext.isValid()) {
                String message = "00-" + spanContext.getTraceId() + "-" + spanContext.getSpanId() + "-01";

                log.info("Отправляем message:{}", message);
            } else {
                log.warn("Не удалось получить message");
            }
            TransactionExchangeDto response = salesPointClientService.sendTransactionWithRetry(exchangeDto);
            transactionExchangeIbDto.setId(savedTransaction.getId()); // ← ЭТО ВАЖНО
            issuingBankClientService.sendTransactionWithRabbitMq(transactionExchangeIbDto);


            if (response == null) {
                log.warn("SalesPoint недоступен, транзакция сохранена локально, но не отправлена.");
                // Можно вернуть Optional.empty() или сохранить статус отправки в базу
                return Optional.empty();
            }


            return Optional.of(response);

        } catch (FeignException fe) {
            span.recordException(fe);
            log.error("Failed to send transaction to sales-point (Feign): {}", fe.getMessage(), fe);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to forward transaction to sales-point", fe);
        } catch (DataIntegrityViolationException dive) {
            span.recordException(dive);
            log.error("Transaction already exists or violates DB constraints: {}", dive.getMessage(), dive);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction violates constraints", dive);
        } catch (Exception e) {
            span.setStatus(StatusCode.ERROR);
            span.recordException(e);
            log.error("Unexpected error during transaction processing: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", e);
        } finally {
            span.end();
        }
    }


    private void updateAccountBalance(Transaction transaction) {
        if (transaction.getTransactionType() == null || transaction.getTransactionType().getId() == null) {
            log.error("TransactionType or its ID is null for transaction id: {}", transaction.getId());
            throw new IllegalArgumentException("TransactionType or its ID is null");
        }
        Long itemId = transaction.getTransactionType().getId();

        Optional<TransactionTypeDto> tt = transactionTypeService.findById(itemId);
        String operator = null;
        if (tt.isPresent()) {
            operator = tt.get().getOperator();
        } else {
            log.error("TransactionType not found for id: {}", transaction.getTransactionType().getId());
            throw new EntityNotFoundException("TransactionType not found");
        }

        if (operator != null) {
            operator = operator.trim();
        }

        BigDecimal amount = transaction.getSum();

        log.info("Transaction operator: '{}', amount: {}", operator, amount);

        if ("-".equals(operator)) {
            accountService.withdraw(transaction.getAccount().getId(), amount);
        } else {
            accountService.deposit(transaction.getAccount().getId(), amount);
        }
    }

    private TransactionExchangeDto convertToExchangeDto(TransactionDto dto) {
        TransactionExchangeDto exchangeDto = new TransactionExchangeDto();
        // маппинг полей
        exchangeDto.setId(dto.getId());
        exchangeDto.setTransactionDate(dto.getTransactionDate());
        exchangeDto.setSum(dto.getSum());
        exchangeDto.setTransactionTypeId(dto.getTransactionType().getId());
        exchangeDto.setCardId(dto.getCard().getId());
        exchangeDto.setAuthorizationCode(dto.getAuthorizationCode());
        exchangeDto.setResponseCodeId(dto.getResponseCode().getId());
        exchangeDto.setTerminalId(dto.getTerminal().getId());
        return exchangeDto;
    }


    private TransactionExchangeIbDto convertToTransactionExchangeIbDto(TransactionDto dto) {
        TransactionExchangeIbDto transactionExchangeIbDto = new TransactionExchangeIbDto();
        // маппинг полей
        transactionExchangeIbDto.setId(dto.getId());
        transactionExchangeIbDto.setTransactionDate(Date.valueOf(dto.getTransactionDate()));
        transactionExchangeIbDto.setSum(dto.getSum());
        transactionExchangeIbDto.setTransactionType(dto.getTransactionType().getId());
        transactionExchangeIbDto.setAccount(dto.getAccount().getId());
        transactionExchangeIbDto.setTransactionName(dto.getTransactionName());
        //transactionExchangeIbDto.setReceivedFromProcessingCenter(Timestamp.valueOf(LocalDateTime.now()));
        transactionExchangeIbDto.setReceivedFromProcessingCenter(OffsetDateTime.now());
        //transactionExchangeIbDto.setSentToProcessingCenter(Timestamp.valueOf(LocalDateTime.now()));
        transactionExchangeIbDto.setSentToProcessingCenter(OffsetDateTime.now());
        return transactionExchangeIbDto;
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public Optional<TransactionDto> update(Long id, TransactionDto dto) {
        Optional<Transaction> exist = transactionRepository.findById(dto.getId());
        if (exist.isEmpty()) {
            log.info("Transaction does not exist with id: {}", dto.getId());
            return Optional.empty();
        }
        Transaction saved = transactionRepository.saveAndFlush(transactionMapper.toEntity(dto));
        log.info("Updated transaction: {}", saved);
        return Optional.empty();
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<Transaction> exist = transactionRepository.findById(id);
        if (exist.isPresent()) {
            transactionRepository.delete(exist.get());
            log.info("Transaction deleted: {} successfully", id);
            return true;
        }
        log.info("Transaction not deleted with id: {}", id);
        return false;
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            transactionRepository.deleteAll();
            log.info("Transaction deleted all successfully");
            return true;
        } catch (EntityNotFoundException e) {
            log.info("Transaction not deleted, cause: {} ", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            transactionRepository.dropTable();
            log.info("Transaction table dropped successfully");
            return true;
        } catch (EntityNotFoundException e) {
            log.info("Transaction not dropped, cause: {} ", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            transactionRepository.createTable();
            log.info("Transaction table created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Transaction not created, cause: {} ", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allTransactionsCache", "transactionByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            transactionRepository.insertDefaultValues();
            log.info("Transaction table initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Transaction not inserted, cause: {} ", e.getMessage());
            return false;
        }
    }
}
