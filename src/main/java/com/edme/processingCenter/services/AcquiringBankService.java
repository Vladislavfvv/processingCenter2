package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.AcquiringBankDto;
import com.edme.processingCenter.mappers.AcquiringBankMapper;
import com.edme.processingCenter.models.AcquiringBank;
import com.edme.processingCenter.repositories.AcquiringBankRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AcquiringBankService implements AbstractService<Long, AcquiringBankDto> {

    @Autowired
    private final AcquiringBankRepository acquiringBankRepository;
    @Autowired
    private final AcquiringBankMapper acquiringBankMapper;

    // Кеширует список всех банков
    @Override
    @Cacheable(value = "allBanksCache")
    public List<AcquiringBankDto> findAll() {
        DelayForTestSwagger.simulateSlowService(); // (для демонстрации задержки)
        return acquiringBankRepository.findAll()
                .stream()
                .map(acquiringBankMapper::toDto)
                .collect(Collectors.toList());
    }

    // Задержка для демонстрации кеша
//    private void simulateSlowService() {
//        long start = System.currentTimeMillis();
//        log.info("Slow getting info from service. Start time: {}", start);
//        try {
//            Thread.sleep(5000L);
//            long end = System.currentTimeMillis();
//            log.info("Slow getting info from service. End time{}", end);
//        } catch (InterruptedException e) {
//            throw new IllegalStateException(e);
//        }
//    }
    @Override
    // Кеширует банк по id
    // Если уже есть результат — возвращает его из кеша.
    @Cacheable(value = "bankByIdCache", key = "#id")
    public Optional<AcquiringBankDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService(); // (для демонстрации задержки)
        return acquiringBankRepository.findById(id)
                .map(acquiringBankMapper::toDto);
    }
    @Override
    @Transactional
    // Удаление из кеша при сохранении нового банка
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public Optional<AcquiringBankDto> save(AcquiringBankDto dto) {

        // Если ID задан — проверяем, не существует ли уже банк
        Optional<AcquiringBank> exiting = acquiringBankRepository.findByBic(dto.getBic());
        if (exiting.isPresent()) {
            log.info("Acquiring bank with ID {} already exists", dto.getId());
            return Optional.empty();
        }

        // Преобразуем DTO в сущность и сохраняем
        AcquiringBank entity = acquiringBankMapper.toEntity(dto);
        entity.setId(null); // Явно обнуляем ID, чтобы JPA сгенерировала новый

        AcquiringBank saved = acquiringBankRepository.saveAndFlush(entity); // теперь saved содержит сгенерированный ID

        log.info("Acquiring bank {} saved", saved.getId());
        return Optional.of(acquiringBankMapper.toDto(saved));
    }

    @Override
    @Transactional
    // Очистка кеша при обновлении
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public Optional<AcquiringBankDto> update(Long id, AcquiringBankDto dto) {
        return acquiringBankRepository.findById(id).map(entity -> {
            entity.setBic(dto.getBic());
            entity.setAbbreviatedName(dto.getAbbreviatedName());
            AcquiringBank saved = acquiringBankRepository.save(entity);
            log.info("Acquiring bank {} updated successfully", saved.getId());
            return acquiringBankMapper.toDto(saved);
        });
    }
    @Override
    @Transactional
    // Очистка кеша при удалении
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<AcquiringBankDto> exiting = findById(id);
        if (exiting.isPresent()) {
            acquiringBankRepository.deleteById(id);
            log.info("Acquiring bank {} deleted successfully", id);
            return true;
        } else {
            log.info("Acquiring bank {} not found", id);
            return false;
        }
    }
    @Override
    @Transactional
    // Очистка кеша при удалении
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            acquiringBankRepository.deleteAll();
            log.info("All acquiring banks deleted successfully");
            return true;
        } catch (Exception e) {
            log.error("Error deleting all AcquiringBanks {}", e.getMessage());
            return false;
        }
    }
    @Override
    @Transactional
    // Очистка кеша при удалении
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            acquiringBankRepository.dropTable();
            log.info("Table AcquiringBank dropped successfully");
            return true;
        } catch (Exception e) {
            log.error("Error dropping Table AcquiringBanks {}", e.getMessage());
            return false;
        }
    }
    @Override
    @Transactional
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public boolean createTable() {
        try {
            acquiringBankRepository.createTable();
            log.info("Table AcquiringBank created successfully");
            return true;
        } catch (Exception e) {
            log.error("Error creating Table AcquiringBanks {}", e.getMessage());
            return false;
        }
    }
    @Override
    @Transactional
    // Очистка кеша при удалении
    @CacheEvict(value = {"allBanksCache", "bankByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            acquiringBankRepository.insertDefaultValues();
            log.info("Default values inserted into AcquiringBank table successfully");
            return true;
        } catch (Exception e) {
            log.error("Error inserting default values into AcquiringBank table: {}", e.getMessage());
            return false;
        }
    }
}
