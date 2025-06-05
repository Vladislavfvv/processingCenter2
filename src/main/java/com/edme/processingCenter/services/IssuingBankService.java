package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.IssuingBankDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.IssuingBankMapper;
import com.edme.processingCenter.models.IssuingBank;
import com.edme.processingCenter.repositories.IssuingBankRepository;
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
public class IssuingBankService implements AbstractService<Long, IssuingBankDto> {

    private final IssuingBankRepository issuingBankRepository;
    private final IssuingBankMapper issuingBankMapper;

    @Override
    @Cacheable(value = "allIssuingBanksCache")
    public List<IssuingBankDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return issuingBankRepository.findAll()
                .stream()
                .map(issuingBankMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "issuingBankByIdCache", key = "#id")
    public Optional<IssuingBankDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService();
        return issuingBankRepository.findById(id)
                .map(issuingBankMapper::toDto);
    }

    @Override
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public Optional<IssuingBankDto> save(IssuingBankDto dto) {
        Optional<IssuingBank> existing = issuingBankRepository.findById(dto.getId());
        if (existing.isPresent()) {
            log.info("IssuingBank with id {} already exists", dto.getId());
            return Optional.empty();
        }
        IssuingBank saved = issuingBankRepository.save(issuingBankMapper.toEntity(dto));
        log.info("IssuingBank saved with id {}", saved.getId());
        return Optional.of(issuingBankMapper.toDto(saved));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public Optional<IssuingBankDto> update(Long id, IssuingBankDto dto) {
        Optional<IssuingBank> existing = issuingBankRepository.findById(id);
        if (existing.isEmpty()) {
            log.info("IssuingBank with id {} does not exist", id);
            return Optional.empty();
        }

        IssuingBank bank = existing.get();
        bank.setBic(dto.getBic());
        bank.setBin(dto.getBin());
        bank.setAbbreviatedName(dto.getAbbreviatedName());

        IssuingBank updated = issuingBankRepository.saveAndFlush(bank);
        log.info("IssuingBank with id {} updated", id);
        return Optional.of(issuingBankMapper.toDto(updated));
    }

    @Override
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<IssuingBank> existing = issuingBankRepository.findById(id);
        if (existing.isPresent()) {
            issuingBankRepository.deleteById(id);
            log.info("IssuingBank with id {} deleted successfully", id);
            return true;
        }
        log.info("IssuingBank with id {} does not exist", id);
        return false;
    }

    @Override
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            issuingBankRepository.deleteAll();
            log.info("All issuing banks have been deleted successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Issuing banks not deleted, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            issuingBankRepository.deleteAll(); // Здесь можно вызывать кастомный SQL если есть
            log.info("IssuingBank table dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Failed to drop IssuingBank table, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            // Обычно создание таблиц обрабатывается миграциями
            issuingBankRepository.deleteAll(); // как заглушка
            log.info("IssuingBank table created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Failed to create IssuingBank table, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allIssuingBanksCache", "issuingBankByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            issuingBankRepository.deleteAll(); // если нужно очистить и наполнить базой
            log.info("IssuingBank table initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Failed to initialize IssuingBank table, cause {}", e.getMessage());
            return false;
        }
    }
}