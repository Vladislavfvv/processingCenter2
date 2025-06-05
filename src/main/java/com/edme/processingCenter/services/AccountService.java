package com.edme.processingCenter.services;

import com.edme.processingCenter.dto.AccountDto;
import com.edme.processingCenter.exceptions.ResourceNotFoundException;
import com.edme.processingCenter.mappers.AccountMapper;
import com.edme.processingCenter.mappers.CurrencyMapper;
import com.edme.processingCenter.mappers.IssuingBankMapper;
import com.edme.processingCenter.models.Account;
import com.edme.processingCenter.repositories.AccountRepository;
import com.edme.processingCenter.utils.DelayForTestSwagger;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService  implements AbstractService<Long, AccountDto>{

    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final IssuingBankService issuingBankService;
    private final AccountMapper accountMapper;
    private final CurrencyMapper currencyMapper;
    private final IssuingBankMapper issuingBankMapper;


    @GetMapping
    @Override
    @Cacheable(value = "allAccountsCache")
    public List<AccountDto> findAll() {
        DelayForTestSwagger.simulateSlowService();
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "accountByIdCache", key = "#id")
    public Optional<AccountDto> findById(Long aLong) {
        DelayForTestSwagger.simulateSlowService();
        return accountRepository.findById(aLong)
                .map(accountMapper::toDto);
    }


    @Override
    //удаление кэша при сохранении нового
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    public Optional<AccountDto> save(AccountDto dto) {
        Optional<Account> existing = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (existing.isPresent()) {
            log.info("Account already exists with account number: {}", dto.getAccountNumber());
            return Optional.empty();
        }
        dto.setId(null);
        Account saved = accountRepository.saveAndFlush(accountMapper.toEntity(dto));
        log.info("Saved account id: {}", saved.getId());
        return Optional.ofNullable(accountMapper.toDto(saved));
    }


    private void updateEntityFromDto(Account account, AccountDto dto) {
        account.setAccountNumber(dto.getAccountNumber());
        account.setBalance(dto.getBalance());

        account.setCurrency(currencyService.findById(dto.getCurrency().getId())
                .map(currencyMapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + dto.getCurrency().getId())));

        account.setIssuingBank(issuingBankService.findById(dto.getIssuingBank().getId())
                .map(issuingBankMapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException("IssuingBank not found with id: " + dto.getIssuingBank().getId())));
    }

    //удаление кэша при обновлении
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    @Override
    public Optional<AccountDto> update(Long id, AccountDto dto) {
        Optional<Account> existing = accountRepository.findById(id);
        if (existing.isPresent()) {
            Account account = existing.get();
            updateEntityFromDto(account, dto);
            Account saved = accountRepository.saveAndFlush(account);
            log.info("Updated successfully account with id: {}", saved.getId());
            return Optional.ofNullable(accountMapper.toDto(saved));
        }
        log.info("Account not updates, cause it not exists");
        return Optional.empty();
    }

    //удаление кэша при удалении
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    @Override
    public boolean delete(Long id) {
        Optional<Account> existing = accountRepository.findById(id);
        if (existing.isPresent()) {
            accountRepository.delete(existing.get());
            log.info("Account with id {} deleted successfully", id);
            return true;
        }
        log.info("Account not deleted");
        return false;
    }

    //удаление кэша при обновлении
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    @Override
    public boolean deleteAll() {
        try {
            accountRepository.deleteAll();
            log.info("All accounts deleted successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.error("All accounts not deleted, cause: {}", e.getMessage());
            return false;
        }
    }

    //удаление кэша при обновлении
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    @Override
    public boolean dropTable() {
        try {
            accountRepository.dropTable();
            log.info("Table accounts dropped successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.warn("Table accounts not dropped, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean createTable() {
        try {
            accountRepository.createTable();
            log.info("Table accounts created successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.warn("Table accounts not created, cause: {}", e.getMessage());
            return false;
        }
    }

    @Override
    //удаление кэша при обновлении
    @CacheEvict(value = {"allAccountsCache", "accountByIdCache"}, allEntries = true)
    public boolean initializeTable() {
        try {
            accountRepository.insertDefaultValues();
            log.info("Table accounts initialized successfully");
            return true;
        } catch (ResourceNotFoundException e) {
            log.warn("Table accounts not initialized, cause: {}", e.getMessage());
            return false;
        }
    }

}
