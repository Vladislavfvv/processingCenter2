package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.PaymentSystemDto;
import com.edme.processingCenter.services.PaymentSystemService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/paymentSystem")
public class PaymentSystemController {

    private PaymentSystemService paymentSystemService;

    @GetMapping
    public ResponseEntity<List<PaymentSystemDto>> getAllPaymentSystems() {
        return ResponseEntity.ok(paymentSystemService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystemDto> getPaymentSystemById(@PathVariable("id") Long id) {
        return paymentSystemService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaymentSystemDto> savePaymentSystem(@RequestBody @Valid PaymentSystemDto paymentSystemDto) {
        log.debug("Saving payment system: {}", paymentSystemDto);
        return paymentSystemService.save(paymentSystemDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePaymentSystem(@PathVariable Long id, @RequestBody @Valid PaymentSystemDto paymentSystemDto) {
        log.debug("Updating payment system: {}", paymentSystemDto);
        return paymentSystemService.update(id, paymentSystemDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createTablePaymentSystems")
    public ResponseEntity<String> createTablePaymentSystems() {
        log.debug("Creating table payment systems");
        return paymentSystemService.createTable()
                ? ResponseEntity.ok("Creating table payment systems")
                : ResponseEntity.badRequest().body("Creating table payment systems failed");
    }

    @PostMapping("/fillTablePaymentSystems")
    public ResponseEntity<String> fillTablePaymentSystems() {
        log.debug("Filling table payment systems");
        return paymentSystemService.initializeTable()
                ? ResponseEntity.ok("Filling table payment systems")
                : ResponseEntity.badRequest().body("Filling table payment systems failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePaymentSystem(@PathVariable Long id) {
        log.debug("Deleting payment system: {}", id);
        return paymentSystemService.delete(id)
                ? ResponseEntity.ok("Deleting payment system successfully")
                : ResponseEntity.badRequest().body("Deleting payment system failed");
    }

    @DeleteMapping("/clearTablePaymentSystem")
    public ResponseEntity<String> clearTablePaymentSystems() {
        log.debug("Clearing table payment systems");
        return paymentSystemService.deleteAll()
                ? ResponseEntity.ok("Clearing table payment systems")
                : ResponseEntity.badRequest().body("Clearing table payment systems failed");
    }

    @DeleteMapping("/dropTablePaymentSystem")
    public ResponseEntity<String> dropTablePaymentSystems() {
        log.debug("Dropping table payment systems");
        return paymentSystemService.dropTable()
                ? ResponseEntity.ok("Dropping table payment systems")
                : ResponseEntity.badRequest().body("Dropping table payment systems failed");
    }
}
