package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.ResponseCodeDto;
import com.edme.processingCenter.services.ResponseCodeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/responseCode")
public class ResponseCodeController {

    private ResponseCodeService responseCodeService;

    @GetMapping
    public ResponseEntity<List<ResponseCodeDto>> getAllResponseCodes() {
        return ResponseEntity.ok(responseCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCodeDto> findByIdResponseCode(@RequestParam("id") Long id) {
        return responseCodeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveResponseEntity(@RequestBody @Valid ResponseCodeDto dto) {
        return ResponseEntity.ok(responseCodeService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResponseEntity(@PathVariable Long id, @RequestBody @Valid ResponseCodeDto dto) {
        return responseCodeService.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createTableResponseCode")
    public ResponseEntity<String> createTableResponseCode() {
        log.info("createTableResponseCode");
        if (responseCodeService.createTable()) {
            return ResponseEntity.ok("Successfully created table ResponseCode");
        } else {
            return ResponseEntity.badRequest().body("Failed to create table ResponseCode");
        }
    }

    @PostMapping("/fillTableResponseCode")
    public ResponseEntity<String> fillTableResponseCode() {
        log.info("Filling table ResponseCode");
        if (responseCodeService.initializeTable()) {
            return ResponseEntity.ok("Successfully filled table ResponseCode");
        } else {
            return ResponseEntity.status(500).body("Failed to fill table ResponseCode");
        }
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteResponseCode(@RequestParam Long id) {
        return responseCodeService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clearTableResponseCode")
    public ResponseEntity<String> clearTableResponseCode() {
        log.info("Clearing table ResponseCode");
        return responseCodeService.deleteAll()
                ? ResponseEntity.ok("Successfully cleared table ResponseCode")
                : ResponseEntity.status(500).body("Failed to clear table ResponseCode");
    }

    @DeleteMapping("/dropTableResponseCode")
    public ResponseEntity<String> dropTableResponseCode() {
        log.info("Dropping table ResponseCode");
        return responseCodeService.dropTable()
                ? ResponseEntity.ok("Successfully dropped table ResponseCode")
                : ResponseEntity.status(500).body("Failed to dropped table ResponseCode");
    }
}
