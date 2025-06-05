package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.CurrencyDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.CurrencyMapper;
import com.edme.processingCenter.models.Currency;
import com.edme.processingCenter.repositories.CurrencyRepository;
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
public class CurrencyService implements AbstractService<Long, CurrencyDto> {


    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @Override
    @Cacheable(value = "allCurrenciesCache")
    public List<CurrencyDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return currencyRepository.findAll()
                .stream()
                .map(currencyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "CurrencyByIdCache", key = "#id")
    public Optional<CurrencyDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return currencyRepository.findById(aLong)
                .map(currencyMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public Optional<CurrencyDto> save(CurrencyDto dto) {
        Optional<Currency> existing = currencyRepository.findById(dto.getId());
        if (existing.isPresent()) {
            log.info("Currency with id {} already exists", dto.getId());
            return Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public Optional<CurrencyDto> update(Long id, CurrencyDto dto) {
        Optional<Currency> existing = currencyRepository.findById(id);
        if (existing.isEmpty()) {
            log.info("Currency with id {} does not exist", id);
            return Optional.empty();
        }
        Currency currency = existing.get();
        currency.setCurrencyDigitalCode(dto.getCurrencyDigitalCode());
        currency.setCurrencyLetterCode(dto.getCurrencyLetterCode());
        currency.setCurrencyDigitalCodeAccount(dto.getCurrencyDigitalCodeAccount());
        currency.setCurrencyName(dto.getCurrencyName());
        Currency updatedCurrency = currencyRepository.saveAndFlush(currency);
        log.info("Currency with id {} updated successfully", id);
        return Optional.of(currencyMapper.toDto(updatedCurrency));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<Currency> existing = currencyRepository.findById(id);
        if (existing.isPresent()) {
            currencyRepository.deleteById(id);
            log.info("Currency with id {} deleted successfully", id);
            return true;
        }
        log.info("Currency with id {} does not exist", id);
        return false;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            currencyRepository.deleteAll();
            log.info("All currencies have been deleted successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("All currencies have not been deleted, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            currencyRepository.deleteAll();
            log.info("Table currencies have been dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table currencies have not been dropped, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            currencyRepository.deleteAll();
            log.info("Table currencies have been created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table currencies have not been created, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCurrenciesCache", "CurrencyByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try{
            currencyRepository.deleteAll();
            log.info("Table currencies have been initialized successfully");
            return true;
        }catch (ResourceNotFoundException e){
            log.info("Table currencies have not been initialized, cause {}", e.getMessage());
            return false;
        }
    }
}
