package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.AccountDto;
import com.edme.processingCenter.mappers.AccountMapper;
import com.edme.processingCenter.services.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    private AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("id") long id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountDto accountDto) {
        return ResponseEntity.ok(accountService.save(accountDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateAccount(@PathVariable("id") long id, @RequestBody @Valid AccountDto accountDto) {
        return accountService.update(id, accountDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/createTableAccounts")
    public ResponseEntity<?> createTableAccounts(@RequestBody @Valid List<AccountDto> accountDtos) {
        log.info("Creating table accounts");
        if (accountService.createTable()) {
            return ResponseEntity.ok("Successfully created table accounts");
        } else {
            return ResponseEntity.badRequest().body("Failed to create table accounts");
        }
    }

    @PostMapping("/fillTableAccounts")
    public ResponseEntity<String> fillTableAccounts() {
        log.info("Filling table accounts");

        if (accountService.initializeTable()) {
            return ResponseEntity.ok("Successfully initialized table Accounts");
        } else {
            return ResponseEntity.status(500).body("Failed to initialize table Accounts");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        return accountService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/clearTableAccounts")
    public ResponseEntity<String> clearTableAccounts() {
        log.info("Clearing table accounts");
        if (accountService.initializeTable()) {
            return ResponseEntity.ok("Successfully cleared table Accounts");
        }
        return ResponseEntity.status(500).body("Failed to clear table Accounts");
    }

    @DeleteMapping("/dropTableAccess")
    public ResponseEntity<String> dropTableAccess() {
        log.info("Dropping table access");
        if (accountService.initializeTable()) {
            return ResponseEntity.ok("Successfully dropped table access");
        }
        return ResponseEntity.status(500).body("Failed to drop table access");
    }
}
