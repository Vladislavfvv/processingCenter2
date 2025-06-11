package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.TransactionTypeDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.TransactionTypeMapper;
import com.edme.processingCenter.models.TransactionType;
import com.edme.processingCenter.repositories.TransactionTypesRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
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
public class TransactionTypeService implements AbstractService<Long, TransactionTypeDto> {


    private final TransactionTypesRepository transactionTypesRepository;
    private final TransactionTypeMapper transactionTypeMapper;


    @Override
    @Cacheable(value = "allTransactionTypesCache")
    public List<TransactionTypeDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return transactionTypesRepository.findAll()
                .stream()
                .map(transactionTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
            @Cacheable(value = "transactionTypeByIdCache", key = "#id", condition = "#id != null")
    public Optional<TransactionTypeDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return transactionTypesRepository.findById(aLong)
                .map(transactionTypeMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public Optional<TransactionTypeDto> save(TransactionTypeDto dto) {
        Optional<TransactionType> existing = transactionTypesRepository.findById(dto.getId());
        if (existing.isPresent()) {
            log.info("TransactionTypeDto {} already exists", dto.getId());
            return Optional.empty();
        }
        dto.setId(null);
        TransactionType saved = transactionTypesRepository.saveAndFlush(transactionTypeMapper.toEntity(dto));
        log.info("TransactionTypeDto {} saved", saved.getId());
        return Optional.of(transactionTypeMapper.toDto(saved));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public Optional<TransactionTypeDto> update(Long id, TransactionTypeDto dto) {
        Optional<TransactionType> existing = transactionTypesRepository.findById(id);
        if (existing.isEmpty()) {
            log.info("TransactionTypeDto {} does not exist", dto.getId());
            return Optional.empty();
        }
        TransactionType transactionType = existing.get();
        dto.setTransactionTypeName(transactionType.getTransactionTypeName());
        TransactionType updated = transactionTypesRepository.saveAndFlush(transactionTypeMapper.toEntity(dto));
        log.info("TransactionTypeDto {} updated", updated.getId());
        return Optional.of(transactionTypeMapper.toDto(updated));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<TransactionType> existing = transactionTypesRepository.findById(id);
        if (existing.isEmpty()) {
            log.info("TransactionTypeDto {} does not exist", id);
            return false;
        }
        transactionTypesRepository.deleteById(id);
        log.info("TransactionTypeDto {} deleted", id);
        return true;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            transactionTypesRepository.deleteAll();
            log.info("All transactionTypes {} deleted successfully", transactionTypesRepository.findAll().size());
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("TransactionTypes not found, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            transactionTypesRepository.dropTable();
            log.info("Table transactionTypes {} dropped successfully", transactionTypesRepository.findAll().size());
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table transactionTypes not found, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            transactionTypesRepository.createTable();
            log.info("Table transactionTypes created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table transactionTypes not created, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTransactionTypesCache", "transactionTypeByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            transactionTypesRepository.insertDefaultValues();
            log.info("Table transactionTypes initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table transactionTypes not initialized, cause {}", e.getMessage());
            return false;
        }
    }
}
