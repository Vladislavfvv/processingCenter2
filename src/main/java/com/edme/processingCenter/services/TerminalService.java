package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.TerminalDto;
import com.edme.processingCenter.mappers.MerchantCategoryCodeMapper;
import com.edme.processingCenter.mappers.SalesPointMapper;
import com.edme.processingCenter.mappers.TerminalMapper;
import com.edme.processingCenter.models.MerchantCategoryCode;
import com.edme.processingCenter.models.SalesPoint;
import com.edme.processingCenter.models.Terminal;
import com.edme.processingCenter.repositories.MerchantCategoryCodeRepository;
import com.edme.processingCenter.repositories.SalesPointRepository;
import com.edme.processingCenter.repositories.TerminalRepository;
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
public class TerminalService implements AbstractService<Long, TerminalDto> {

    private final TerminalRepository terminalRepository;
    private final TerminalMapper terminalMapper;
    private final SalesPointRepository salesPointRepository;
    private final MerchantCategoryCodeRepository merchantCategoryCodeRepository;
    private final SalesPointMapper salesPointMapper;
    private final MerchantCategoryCodeMapper merchantCategoryCodeMapper;

    @Override
    @Cacheable(value = "allTerminalsCache")
    public List<TerminalDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return terminalRepository.findAll()
                .stream().map(terminalMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "transactionByIdCache", key = "#id")
    public Optional<TerminalDto> findById(Long id) {
        DelayForTestSwagger.simulateSlowService();
        return terminalRepository.findById(id)
                .map(terminalMapper::toDto);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public Optional<TerminalDto> save(TerminalDto dto) {
        Optional<Terminal> terminal = terminalRepository.findById(dto.getId());
        if (terminal.isPresent()) {
            log.info("Terminal already exists: {}", dto);
            return Optional.empty();
        } else {
            dto.setId(null);

            Terminal entity = terminalMapper.toEntity(dto);
            Terminal saved = terminalRepository.saveAndFlush(entity);

            log.info("Terminal saved: {}", entity);
            return Optional.of(terminalMapper.toDto(saved));
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public Optional<TerminalDto> update(Long id, TerminalDto dto) {
        Optional<Terminal> optionalTerminal = terminalRepository.findById(id);

        if (optionalTerminal.isEmpty()) {
            log.info("Terminal with id {} not found, update aborted", id);
            return Optional.empty();
        }

        Terminal terminal = optionalTerminal.get();

        terminal.setTerminalId(dto.getTerminalId());

        MerchantCategoryCode mccEntity = merchantCategoryCodeRepository.findById(dto.getMcc().getId())
                .orElseThrow(() -> new EntityNotFoundException("MCC not found with id " + dto.getMcc().getId()));
        SalesPoint salesPointEntity = salesPointRepository.findById(dto.getSalesPoint().getId())
                .orElseThrow(() -> new EntityNotFoundException("SalesPoint not found with id " + dto.getSalesPoint().getId()));

        terminal.setMcc(mccEntity);
        terminal.setPos(salesPointEntity);

        Terminal updated = terminalRepository.saveAndFlush(terminal);

        log.info("Terminal with id {} successfully updated", updated.getId());

        return Optional.of(terminalMapper.toDto(updated));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public boolean delete(Long id) {
        Optional<TerminalDto> terminal = findById(id);
        if (terminal.isPresent()) {
            terminalRepository.deleteById(id);
            log.info(" successfully", terminal);
            return true;
        } else {
            log.info("Terminal not found: {}", id);
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public boolean deleteAll() {
        try {
            terminalRepository.deleteAll();
            log.info("All records from table Terminal deleted successfully");
            return true;
        } catch (Exception e) {
            log.error("Error deleting all records from table Terminal: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public boolean dropTable() {
        try {
            terminalRepository.dropTable();
            log.info("Dropped table Terminal successfully");
            return true;
        } catch (Exception e) {
            log.error("Error dropping table Terminal : {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean createTable() {
        try {
            terminalRepository.createTable();
            log.info("Created table Terminal successfully");
            return true;
        } catch (Exception e) {
            log.error("Error creating table Terminal : {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"allTerminalsCache", "transactionByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        if (!createTable()) {
            log.error("Error initializing table Terminal  cause table  Terminal not created");
            return false;
        }
        try {
            terminalRepository.insertDefaultValues();
            log.info("Initialized table Terminal successfully");
            return true;
        } catch (Exception e) {
            log.error("Error initializing table Terminal : {}", e.getMessage());
            return false;
        }
    }

}