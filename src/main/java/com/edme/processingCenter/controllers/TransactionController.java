package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.TransactionDto;
import com.edme.processingCenter.services.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable("id") Long id) {
        return transactionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveTransaction(@RequestBody @Valid TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.save(transactionDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable Long id, @RequestBody @Valid TransactionDto transactionDto) {
        return transactionService.update(id, transactionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/createTableTransaction")
    public ResponseEntity<String> createTableTransaction() {
        return transactionService.createTable()
                ? ResponseEntity.ok("Successfully created table Transaction")
                : ResponseEntity.badRequest().body("Failed to create table Transaction");
    }

    @PostMapping("/fillTableTransaction")
    public ResponseEntity<String> fillTableTransaction() {
        return transactionService.initializeTable()
                ? ResponseEntity.ok("Filled table Transaction")
                : ResponseEntity.badRequest().body("Failed to fill table Transaction");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable Long id) {
        return transactionService.delete(id)
                ? ResponseEntity.ok("Successfully deleted transaction with id:" + id)
                : ResponseEntity.badRequest().body("Failed to delete transaction with id:" + id);
    }

    @DeleteMapping("/clearTableTransaction")
    public ResponseEntity<String> clearTableTransaction() {
        return transactionService.deleteAll()
                ? ResponseEntity.ok("Successfully cleared table Transaction")
                : ResponseEntity.badRequest().body("Failed to clear table Transaction");
    }

    @DeleteMapping("/dropTableTransaction")
    public ResponseEntity<String> dropTableTransaction() {
        return transactionService.dropTable()
                ? ResponseEntity.ok("Successfully dropped table Transaction")
                : ResponseEntity.badRequest().body("Failed to drop table Transaction");
    }


}
