package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.CardDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.AccountMapper;
import com.edme.processingCenter.mappers.CardMapper;
import com.edme.processingCenter.mappers.CardStatusMapper;
import com.edme.processingCenter.mappers.PaymentSystemMapper;
import com.edme.processingCenter.models.Card;
import com.edme.processingCenter.repositories.CardRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CardService implements AbstractService<Long, CardDto> {


    @Autowired
    private final CardRepository cardRepository;
    private final CardStatusService cardStatusService;
    private final PaymentSystemService paymentSystemService;
    private final AccountService accountService;
    private final CardMapper cardMapper;
    private final CardStatusMapper cardStatusMapper;
    private final PaymentSystemMapper paymentSystemMapper;
    private final AccountMapper accountMapper;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "allCardsCache")
    public List<CardDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return cardRepository.findAll()
                .stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "cardByIdCache", key = "#id")
    public Optional<CardDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return cardRepository.findById(aLong)
                .map(cardMapper::toDto);
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public Optional<CardDto> save(CardDto dto) {
        Optional<Card> exists = cardRepository.findById(dto.getId());
        if (exists.isPresent()) {
            log.info("Card already exists: {}", exists.get());
            return Optional.empty();
        }
        dto.setId(null);
        Card saved = cardRepository.saveAndFlush(cardMapper.toEntity(dto));
        log.info("Saved Card: {}", saved);
        return Optional.of(cardMapper.toDto(saved));
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public Optional<CardDto> update(Long id, CardDto dto) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isEmpty()) {
            log.info("Card not updated because it does not exist with id: {}", id);
            return Optional.empty();
        }

        Card card = optionalCard.get();

        card.setCardNumber(dto.getCardNumber());
        card.setExpirationDate(dto.getExpirationDate());
        card.setHolderName(dto.getHolderName());

        card.setCardStatus(
                cardStatusService.findById(dto.getCardStatus().getId())
                        .map(cardStatusMapper::toEntity)
                        .orElseThrow(() -> new EntityNotFoundException("CardStatus not found with id: " + dto.getCardStatus().getId()))
        );

        card.setPaymentSystem(
                paymentSystemService.findById(dto.getPaymentSystem().getId())
                        .map(paymentSystemMapper::toEntity)
                        .orElseThrow(() -> new EntityNotFoundException("PaymentSystem not found with id: " + dto.getPaymentSystem().getId()))
        );

        card.setAccount(
                accountService.findById(dto.getAccount().getId())
                        .map(accountMapper::toEntity)
                        .orElseThrow(() -> new EntityNotFoundException("Account not found with id: " + dto.getAccount().getId()))
        );

        card.setReceivedFromIssuingBank(dto.getReceivedFromIssuingBank());
        card.setSentToIssuingBank(dto.getSentToIssuingBank());
        Card updatedCard = cardRepository.saveAndFlush(card);
        log.info("Updated Card: {} with id: {}", updatedCard, id);
        return Optional.of(cardMapper.toDto(updatedCard));
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<Card> exists = cardRepository.findById(id);
        if (exists.isEmpty()) {
            log.info("Card not deleted because its not exist: {}", id);
            return false;
        }
        cardRepository.delete(exists.get());
        log.info("Card deleted: {}", id);
        return true;
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            cardRepository.deleteAll();
            log.info("Table cards deleted.");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table cards not found.");
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            cardRepository.dropTable();
            log.info("Table cards dropped.");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table cards not dropped, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            cardRepository.createTable();
            log.info("Table cards created.");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table cards not created, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allCardsCache", "cardByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            cardRepository.insertDefaultValues();
            log.info("Table cards initialized.");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table cards not initialized, cause {}", e.getMessage());
            return false;
        }
    }
}