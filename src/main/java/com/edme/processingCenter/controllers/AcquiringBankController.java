package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.AcquiringBankDto;
import com.edme.processingCenter.services.AcquiringBankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/acquiringBanks")
public class AcquiringBankController {

    private final AcquiringBankService acquiringBankService;

    @Operation(summary = "Получить все AcquiringBank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "404", description = "AcquiringBank не найдены")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<AcquiringBankDto>> getAllAcquiringBanks() {
        return ResponseEntity.ok(acquiringBankService.findAll());
    }


    @Operation(summary = "Получить AcquiringBank по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "404", description = "AcquiringBank не найден")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AcquiringBankDto> getAcquiringBankById(@PathVariable("id") Long id) {
        return acquiringBankService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveAcquiringBank(@RequestBody @Valid AcquiringBankDto acquiringBank) {
        return ResponseEntity.ok(acquiringBankService.save(acquiringBank));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление данных банка-эквайера", description = "Обновляет инфу о банке-эквайере")
    public ResponseEntity<AcquiringBankDto> updateAcquiringBank(@PathVariable("id") Long id, @RequestBody @Valid AcquiringBankDto acquiringBankDto) {
        return acquiringBankService.update(id, acquiringBankDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }


    @Operation(summary = "Сохранить AcquiringBank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "AcquiringBank не сохранен")
    })
    @PostMapping("/createTableAcquiringBank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createTableAcquiringBank() {
        log.info("Creating table acquiring bank");
        if (acquiringBankService.createTable()) {
            return ResponseEntity.ok("Successfully created table AcquiringBank");
        } else {
            return ResponseEntity.badRequest().body("Failed to create table AcquiringBank");
        }
    }

    @PostMapping("/fillTableAcquiringBank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> fillTableAcquiringBank() {
        log.info("Filling table acquiring bank");
        createTableAcquiringBank();
        if (acquiringBankService.initializeTable()) {
            return ResponseEntity.ok("Successfully filled table AcquiringBank");
        } else {
            return ResponseEntity.status(500).body("Failed to fill table AcquiringBank");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AcquiringBankDto> deleteAcquiringBank(@PathVariable Long id) {
        return acquiringBankService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clearTableAcquiringBank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> clearTableAcquiringBank() {
        log.info("Clearing table acquiring bank");
        if (acquiringBankService.deleteAll()) {
            return ResponseEntity.ok("Successfully cleared table AcquiringBank");
        } else {
            return ResponseEntity.status(500).body("Failed to clear table AcquiringBank");
        }
    }

    @DeleteMapping("/dropTableAcquiringBank")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> dropTableAcquiringBank() {
        log.info("Dropping table acquiring bank");
        if (acquiringBankService.dropTable()) {
            return ResponseEntity.ok("Successfully dropped table AcquiringBank");
        } else {
            return ResponseEntity.status(500).body("Failed to dropped table AcquiringBank");
        }
    }
}
