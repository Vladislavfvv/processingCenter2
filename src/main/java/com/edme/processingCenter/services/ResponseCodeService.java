package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.ResponseCodeDto;
import com.edme.processingCenter.mappers.ResponseCodeMapper;
import com.edme.processingCenter.repositories.ResponseCodeRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
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
public class ResponseCodeService implements AbstractService<Long, ResponseCodeDto> {

    @Autowired
    private final ResponseCodeRepository responseCodeRepository;
    @Autowired
    private final ResponseCodeMapper responseCodeMapper;

    @Override
    @Cacheable(value = "allResponseCodesCache")
    public List<ResponseCodeDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return responseCodeRepository.findAll()
                .stream().map(responseCodeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "responseCodeByIdCache", key = "#id")
    public Optional<ResponseCodeDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService();
        return responseCodeRepository.findById(id)
                .map(responseCodeMapper::toDto);
    }

    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public Optional<ResponseCodeDto> save(ResponseCodeDto dto) {
        Optional<ResponseCodeDto> exiting = findById(dto.getId());
        if (exiting.isPresent()) {
            log.info("Response code already exists: {}", dto);
            return Optional.empty();
        }
        dto.setId(null);
        responseCodeRepository.saveAndFlush(responseCodeMapper.toEntity(dto));
        log.info("Response code saved: {} successfully", dto);
        return Optional.of(dto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public Optional<ResponseCodeDto> update(Long id, ResponseCodeDto dto) {
        Optional<ResponseCodeDto> exiting = findById(id);
        if (exiting.isPresent()) {
            ResponseCodeDto exitingDto = exiting.get();
            exitingDto.setErrorCode(dto.getErrorCode());
            exitingDto.setErrorLevel(exitingDto.getErrorLevel());
            exitingDto.setErrorDescription(dto.getErrorDescription());
            responseCodeRepository.save(responseCodeMapper.toEntity(dto));
            log.info("ResponseCode bank {} updated successfully", exitingDto.getId());
            return Optional.of(exitingDto);
        } else {
            log.info("Response code not found: {}", dto);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<ResponseCodeDto> exiting = findById(id);
        if (exiting.isPresent()) {
            responseCodeRepository.deleteById(id);
            log.info("ResponseCode bank {} deleted successfully", exiting.get().getId());
            return true;
        }
        log.info("Response code not found: {}", id);
        return false;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            responseCodeRepository.deleteAll();
            log.info("All ResponseCodeBanks deleted successfully");
            return true;
        } catch (Exception e) {
            log.info("Error deleting all ResponseCodeBanks: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            responseCodeRepository.dropTable();
            log.info("Table ResponseCodeBank dropped successfully");
            return true;
        } catch (Exception e) {
            log.info("Error dropping Table ResponseCodeBank: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            responseCodeRepository.createTable();
            log.info("Table ResponseCodeBank created successfully");
            return true;
        } catch (Exception e) {
            log.info("Error creating Table ResponseCodeBank: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allResponseCodesCache", "responseCodeByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        if (!createTable()) {
            log.info("Initialization failed. Table ResponseCodeBank not created");
            return false;
        }
        try {
            responseCodeRepository.insertDefaultValues();
            log.info("Table ResponseCodeBank initialized successfully");
            return true;
        } catch (Exception e) {
            log.info("Error initializing Table ResponseCodeBank: {}", e.getMessage());
            return false;
        }
    }
}
