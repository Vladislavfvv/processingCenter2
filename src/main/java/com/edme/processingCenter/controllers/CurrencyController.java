package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.CurrencyDto;
import com.edme.processingCenter.services.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable("id") Long id) {
        return currencyService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveCurrency(@RequestBody @Valid CurrencyDto currencyDto) {
        log.debug("Save currency: {}", currencyDto);
        return ResponseEntity.ok(currencyService.save(currencyDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(@PathVariable Long id, @RequestBody @Valid CurrencyDto currencyDto) {
        log.debug("Update currency: {}", currencyDto);
        return currencyService.update(id, currencyDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/createTableCurrencies")
    public ResponseEntity<String> createTableCurrencies() {
        log.debug("Create table currencies");
        return currencyService.createTable()
                ? ResponseEntity.ok("Create table currencies")
                : ResponseEntity.badRequest().body("Failed creating table currencies");
    }

    @PostMapping("/fillTableCurrencies")
    public ResponseEntity<String> fillTableCurrencies() {
        log.debug("Fill table currencies");
        return currencyService.initializeTable()
                ? ResponseEntity.ok("Fill table currencies")
                : ResponseEntity.badRequest().body("Failed fill table currencies");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCurrency(@PathVariable Long id) {
        log.debug("Delete currency: {}", id);
        return currencyService.delete(id)
                ? ResponseEntity.ok("Delete currency successfully")
                : ResponseEntity.badRequest().body("Failed deleting currency");
    }

    @DeleteMapping("/clearTableCurrencies")
    public ResponseEntity<String> clearTableCurrencies() {
        log.debug("Clear table currencies");
        return currencyService.deleteAll()
                ? ResponseEntity.ok("Clear table currencies successfully")
                : ResponseEntity.badRequest().body("Failed clear table currencies");
    }

    @DeleteMapping("/dropTableCurrencies")
    public ResponseEntity<String> dropTableCurrencies() {
        log.debug("Drop table currencies");
        return currencyService.dropTable()
                ? ResponseEntity.ok("Drop table currencies successfully")
                : ResponseEntity.badRequest().body("Failed drop table currencies");
    }

}
