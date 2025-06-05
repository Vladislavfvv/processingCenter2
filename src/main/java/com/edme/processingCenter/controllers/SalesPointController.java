package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.SalesPointDto;
import com.edme.processingCenter.services.SalesPointService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/salesPoints")
public class SalesPointController {

    private SalesPointService salesPointService;

    @GetMapping
    public ResponseEntity<List<SalesPointDto>>  getAllSalesPoint() {
       return ResponseEntity.ok(salesPointService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesPointDto> getSalesPointById(@PathVariable("id") Long id) {
        return salesPointService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveSalesPoint(@RequestBody @Valid SalesPointDto salesPointDto) {
        return ResponseEntity.ok(salesPointService.save(salesPointDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesPointDto> updateSalesPoint(@PathVariable("id") Long id, @RequestBody SalesPointDto dto) {
        return salesPointService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/createTableSalesPoint")
    public ResponseEntity<String> createTableSalesPoint() {
        log.info("Creating table sales point");
        return salesPointService.createTable()
                ? ResponseEntity.ok("Table SalesPoint created")
                : ResponseEntity.badRequest().build();
    }
    @PostMapping("/fillTableSalesPoint")
    public ResponseEntity<String> fillTableSalesPoint() {
        log.info("Filling table sales point");
        return salesPointService.initializeTable()
                ? ResponseEntity.ok("Table SalesPoint filled")
                : ResponseEntity.badRequest().body("Failed to fill table SalesPoint");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSalesPointById(@PathVariable("id") Long id) {
        log.info("Deleting sales point by id: {}", id);
        return salesPointService.delete(id)
                ? ResponseEntity.ok("Deleted sales point by id")
                : ResponseEntity.badRequest().body("Failed to delete sales point by id");
    }

    @DeleteMapping("/clearTableSalesPoint")
    public ResponseEntity<String> clearTableSalesPoint() {
        log.info("Clearing table SalesPoint");
        return salesPointService.deleteAll()
                ? ResponseEntity.ok("Table SalesPoint cleared")
                : ResponseEntity.badRequest().body("Failed to clear table SalesPoint");
    }

    @DeleteMapping("/dropTableSalesPoint")
    public ResponseEntity<String> dropTableSalesPoint() {
        log.info("Dropping table SalesPoint");
        return salesPointService.dropTable()
                ? ResponseEntity.ok("Table SalesPoint dropped")
        : ResponseEntity.status(500).body("Failed to drop table SalesPoint");
    }

}
