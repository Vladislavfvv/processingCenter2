package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.MerchantCategoryCodeDto;
import com.edme.processingCenter.mappers.MerchantCategoryCodeMapper;
import com.edme.processingCenter.repositories.MerchantCategoryCodeRepository;
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
public class MerchantCategoryCodeService implements AbstractService<Long, MerchantCategoryCodeDto>{

    @Autowired
    private MerchantCategoryCodeRepository merchantCategoryCodeRepository;
    @Autowired
    private final MerchantCategoryCodeMapper merchantCategoryCodeMapper;

    @Override
    @Cacheable(value = "allMerchantCategoryCodesCache")
    public List<MerchantCategoryCodeDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return merchantCategoryCodeRepository.findAll()
                .stream()
                .map(merchantCategoryCodeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "merchantCategoryCodeByIdCache", key = "#id")
    public Optional<MerchantCategoryCodeDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService();
        return merchantCategoryCodeRepository.findById(id)
                .map(merchantCategoryCodeMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public Optional<MerchantCategoryCodeDto> save(MerchantCategoryCodeDto merchantCategoryCodeDto) {
        Optional<MerchantCategoryCodeDto> existing = findById(merchantCategoryCodeDto.getId());
        if (existing.isPresent()) {
            log.info("MerchantCategoryCodeDto {} already exists.", merchantCategoryCodeDto);
            return Optional.empty();
        } else {
            merchantCategoryCodeRepository.saveAndFlush(merchantCategoryCodeMapper.toEntity(merchantCategoryCodeDto));
            log.info("MerchantCategoryCodeDto {} saved successfully.", merchantCategoryCodeDto);
            return Optional.of(merchantCategoryCodeDto);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public Optional<MerchantCategoryCodeDto> update(Long id, MerchantCategoryCodeDto dto) {
        Optional<MerchantCategoryCodeDto> existing = findById(id);
        if (existing.isPresent()) {
            MerchantCategoryCodeDto merchantCategoryCodeToUpdate = existing.get();
            merchantCategoryCodeToUpdate.setId(null);
            merchantCategoryCodeToUpdate.setMcc(dto.getMcc());
            merchantCategoryCodeToUpdate.setMccName(dto.getMccName());
            merchantCategoryCodeRepository.save(merchantCategoryCodeMapper.toEntity(merchantCategoryCodeToUpdate));
            log.info("MerchantCategoryCodeDto {} updated successfully.", merchantCategoryCodeToUpdate);
            return Optional.of(merchantCategoryCodeToUpdate);
        } else {
            log.info("MerchantCategoryCodeDto {} not found.", id);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<MerchantCategoryCodeDto> existing = findById(id);
        if (existing.isPresent()) {
            merchantCategoryCodeRepository.deleteById(id);
            log.info("MerchantCategoryCodeDto {} deleted successfully.", id);
            return true;
        } else {
            log.info("MerchantCategoryCodeDto {} not found.", id);
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            merchantCategoryCodeRepository.deleteAll();
            log.info("MerchantCategoryCodeDto {} deleted.", merchantCategoryCodeRepository.findAll().size());
            return true;
        } catch (Exception e) {
            log.info("Error deleting all MerchantCategoryCodeDto {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            merchantCategoryCodeRepository.dropTable();
            log.info("Table MerchantCategoryCodeDto dropped successfully");
            return true;
        } catch (Exception e) {
            log.info("Error dropping Table MerchantCategoryCodeDto {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            merchantCategoryCodeRepository.createTable();
            log.info("Table MerchantCategoryCodeDto created successfully");
            return true;
        } catch (Exception e) {
            log.info("Error creating Table MerchantCategoryCodeDto {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allMerchantCategoryCodesCache", "merchantCategoryCodeByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            merchantCategoryCodeRepository.insertDefaultValues();
            log.info("Table MerchantCategoryCodeDto initialized successfully");
            return true;
        } catch (Exception e) {
            log.info("Error initializing Table MerchantCategoryCodeDto {}", e.getMessage());
            return false;
        }
    }

}
