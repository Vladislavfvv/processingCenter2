package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.PaymentSystemDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.PaymentSystemMapper;
import com.edme.processingCenter.models.PaymentSystem;
import com.edme.processingCenter.repositories.PaymentSystemRepository;
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
public class PaymentSystemService implements AbstractService<Long, PaymentSystemDto> {

    private final PaymentSystemRepository paymentSystemRepository;
    private final PaymentSystemMapper paymentSystemMapper;



    @Override
    @Cacheable(value = "allPaymentSystemCache")
    public List<PaymentSystemDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return paymentSystemRepository.findAll()
                .stream()
                .map(paymentSystemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "paymentSystemByIdCache", key = "#id")
    public Optional<PaymentSystemDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return paymentSystemRepository.findById(aLong)
                .map(paymentSystemMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public Optional<PaymentSystemDto> save(PaymentSystemDto dto) {
        Optional<PaymentSystem> exist = paymentSystemRepository.findById(dto.getId());
        if (exist.isPresent()) {
            log.info("Payment system already exists");
            return Optional.empty();
        }
        dto.setId(null);
        PaymentSystem saved = paymentSystemRepository.saveAndFlush(paymentSystemMapper.toEntity(dto));
        log.info("Payment system saved");
        return Optional.of(paymentSystemMapper.toDto(saved));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public Optional<PaymentSystemDto> update(Long id, PaymentSystemDto dto) {
        Optional<PaymentSystem> optionalPaymentSystem = paymentSystemRepository.findById(id);

        if (optionalPaymentSystem.isEmpty()) {
            log.info("PaymentSystem with id {} does not exist, update aborted", id);
            return Optional.empty();
        }

        PaymentSystem paymentSystem = optionalPaymentSystem.get();
        paymentSystem.setPaymentSystemName(dto.getPaymentSystemName());
        PaymentSystem updated = paymentSystemRepository.saveAndFlush(paymentSystem);
        log.info("PaymentSystem with id {} successfully updated to name: {}", id, updated.getPaymentSystemName());

        return Optional.of(paymentSystemMapper.toDto(updated));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<PaymentSystem> exist = paymentSystemRepository.findById(id);
        if (exist.isEmpty()) {
            log.info("PaymentSystem does not exists");
            return false;
        }
        paymentSystemRepository.delete(exist.get());
        log.info("PaymentSystem {} deleted successfully", exist.get().getPaymentSystemName());
        return false;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            paymentSystemRepository.deleteAll();
            log.info("All paymentSystems deleted successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Error deleting all PaymentSystems, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            paymentSystemRepository.dropTable();
            log.info("PaymentSystems table dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Error dropping PaymentSystems table, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            paymentSystemRepository.createTable();
            log.info("PaymentSystems table created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Error creating PaymentSystems, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allPaymentSystemCache", "paymentSystemByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            paymentSystemRepository.insertDefaultValues();
            log.info("PaymentSystems table initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.info("Error initializing PaymentSystems, cause: {}", e.getMessage());
            return false;
        }
    }
}
