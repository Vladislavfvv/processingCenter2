package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.SalesPointDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.AcquiringBankMapper;
import com.edme.processingCenter.mappers.SalesPointMapper;
import com.edme.processingCenter.models.SalesPoint;
import com.edme.processingCenter.repositories.SalesPointRepository;
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
public class SalesPointService implements AbstractService<Long, SalesPointDto> {

    private final SalesPointRepository salesPointRepository;
    private final SalesPointMapper salesPointMapper;
    private final AcquiringBankMapper acquiringBankMapper;
    private final AcquiringBankService acquiringBankService;


    @Override
    @Cacheable(value = "allSalesPointsCache")
    public List<SalesPointDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return salesPointRepository.findAll()
                .stream()
                .map(salesPointMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "salesPointByIdCache", key = "#id")
    public Optional<SalesPointDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService();
        return salesPointRepository.findById(id)
                .map(salesPointMapper::toDto);
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public Optional<SalesPointDto> save(SalesPointDto dto) {
        SalesPoint entity = salesPointMapper.toEntity(dto);
        SalesPoint saved = salesPointRepository.save(entity);
        log.info("SalesPoint with id {} created", saved.getId());
        return Optional.of(salesPointMapper.toDto(saved));
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public Optional<SalesPointDto> update(Long id, SalesPointDto dto) {
        Optional<SalesPoint> existing = salesPointRepository.findById(id);
        if (existing.isEmpty()) {
            log.info("SalesPoint with id {} not found", id);
            return Optional.empty();
        }

        SalesPoint salesPoint = existing.get();
        salesPoint.setPosName(dto.getPosName());
        salesPoint.setPosAddress(dto.getPosAddress());
        salesPoint.setPosInn(dto.getPosInn());
        salesPoint.setAcquiringBank(acquiringBankMapper.toEntity(dto.getAcquiringBank()));

        SalesPoint updated = salesPointRepository.saveAndFlush(salesPoint);
        log.info("SalesPoint with id {} updated", id);
        return Optional.of(salesPointMapper.toDto(updated));
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        if (salesPointRepository.existsById(id)) {
            salesPointRepository.deleteById(id);
            log.info("SalesPoint with id {} deleted successfully", id);
            return true;
        }
        log.info("SalesPoint with id {} does not exist", id);
        return false;
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            salesPointRepository.deleteAll();
            log.info("All salesPoints deleted successfully");
            return true;
        } catch (Exception e) {
            log.error("Failed to delete all SalesPoints: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            salesPointRepository.dropTable();
            log.warn("Table SalesPoints dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.warn("Table SalesPoints not dropped, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            salesPointRepository.createTable();
            log.info("Table SalesPoints created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.warn("Table SalesPoints not created, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = {"allSalesPointsCache", "salesPointByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            salesPointRepository.insertDefaultValues();
            log.info("SalesPoint table initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.error("Failed to initialize sales point table: {}", e.getMessage());
            return false;
        }
    }
}

