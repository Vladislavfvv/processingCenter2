package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.TransactionTypeDto;
import com.edme.processingCenter.services.TransactionTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactionTypes")
public class TransactionTypeController {

    private TransactionTypeService transactionTypeService;

    @GetMapping
    public ResponseEntity<List<TransactionTypeDto>> getAllTransactionTypes() {
        return ResponseEntity.ok(transactionTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionTypeDto> getTransactionTypeById(@PathVariable("id") Long id) {
        return transactionTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveTransactionType(@RequestBody @Valid TransactionTypeDto transactionTypeDto) {
        return ResponseEntity.ok(transactionTypeService.save(transactionTypeDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransactionType(@PathVariable("id") Long id, @RequestBody TransactionTypeDto dto) {
        return transactionTypeService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/createTableTransactionType")
    public ResponseEntity<String> createTableTransactionType() {
        log.info("Creating table transaction type.");
        if (transactionTypeService.createTable()) {
            return ResponseEntity.ok("Successfully created table TransactionType.");
        } else {
            return ResponseEntity.badRequest().body("Failed to create table TransactionType.");
        }
    }

    @PostMapping("/fillTableTransactionType")
    public ResponseEntity<String> fillTableTransactionType() {
        log.info("Filling table transaction type.");
        createTableTransactionType();
        if (transactionTypeService.initializeTable()) {
            return ResponseEntity.ok("Successfully filled table TransactionType.");
        } else {
            return ResponseEntity.status(500).body("Failed to fill table TransactionType.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionTypeDto> deleteTransactionType(@PathVariable Long id) {
        return transactionTypeService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/clearTableTransactionType")
    public ResponseEntity<String> clearTableTransactionType() {
        log.info("Clearing table transaction type.");
        if (transactionTypeService.deleteAll()) {
            return ResponseEntity.ok("Successfully cleared table TransactionType.");
        } else {
            return ResponseEntity.status(500).body("Failed to clear table TransactionType.");
        }
    }

    @DeleteMapping("/dropTableTransactionType")
    public ResponseEntity<String> dropTableTransactionType() {
        log.info("Dropping table transaction type.");
        if (transactionTypeService.dropTable()) {
            return ResponseEntity.ok("Successfully dropped table TransactionType.");
        } else {
            return ResponseEntity.status(500).body("Failed to dropped table TransactionType.");
        }
    }


}
