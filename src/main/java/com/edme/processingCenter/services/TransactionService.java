package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.TransactionDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.AccountMapper;
import com.edme.processingCenter.mappers.TransactionMapper;
import com.edme.processingCenter.mappers.TransactionTypeMapper;
import com.edme.processingCenter.models.Transaction;
import com.edme.processingCenter.repositories.TransactionRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
