package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.CardStatusDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.CardStatusMapper;
import com.edme.processingCenter.models.CardStatus;
import com.edme.processingCenter.repositories.CardStatusRepository;
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
public class CardStatusService implements AbstractService<Long, CardStatusDto> {



    private final CardStatusRepository cardStatusRepository;
    private final CardStatusMapper cardStatusMapper;

    @Override
    @Cacheable(value = "allCardStatusesCache")
    public List<CardStatusDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return cardStatusRepository.findAll()
                .stream()
                .map(cardStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "CardStatusByIdCache", key = "#id")
    public Optional<CardStatusDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return cardStatusRepository.findById(aLong)
                .map(cardStatusMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public Optional<CardStatusDto> save(CardStatusDto dto) {
        Optional<CardStatus> exist = cardStatusRepository.findById(dto.getId());
        if (exist.isPresent()) {
            log.info("CardStatus already exists with id: {} ", exist.get().getId());
            return Optional.empty();
        }
        dto.setId(null);
        CardStatus saved = cardStatusRepository.saveAndFlush(cardStatusMapper.toEntity(dto));
        log.info("CardStatus saved with id: {} ", saved.getId());
        return Optional.ofNullable(cardStatusMapper.toDto(saved));

    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public Optional<CardStatusDto> update(Long id, CardStatusDto dto) {
        Optional<CardStatus> exist = cardStatusRepository.findById(id);
        if (exist.isEmpty()) {
            log.info("CardStatus does not exists with id: {} ", id);
            return Optional.empty();
        }
        CardStatus cardStatus = exist.get();
        cardStatus.setCardStatusName(dto.getCardStatusName());
        CardStatus saved = cardStatusRepository.saveAndFlush(cardStatus);
        log.info("CardStatus saved with id: {} ", saved.getId());
        return Optional.ofNullable(cardStatusMapper.toDto(saved));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<CardStatus> exist = cardStatusRepository.findById(id);
        if (exist.isEmpty()) {
            log.info("CardStatus does not exists with id: {}", id);
            return false;
        }
        cardStatusRepository.delete(exist.get());
        log.info("CardStatus deleted with id: {}", id);
        return true;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            cardStatusRepository.deleteAll();
            log.info("All cardStatuses deleted successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("CardStatuses not deleted, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            cardStatusRepository.dropTable();
            log.info("Table cardStatuses dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Table cardStatuses not dropped, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            cardStatusRepository.createTable();
            log.info("Table cardStatuses created successfully");
            return true;
        }catch (ResourceNotFoundException e) {
            log.info("Table cardStatuses not created, cause {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allCardStatusesCache", "CardStatusByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try{
            cardStatusRepository.createTable();
            log.info("Table cardStatuses initialized successfully");
            return true;
        }catch(ResourceNotFoundException e){
            log.info("Table cardStatuses not initialized, cause {}", e.getMessage());
            return false;
        }
    }
}