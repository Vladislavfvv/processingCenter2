package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.MerchantCategoryCodeDto;
import com.edme.processingCenter.services.MerchantCategoryCodeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/merchantCategoryCodes")
public class MerchantCategoryCodeController {

     private MerchantCategoryCodeService merchantCategoryCodeService;

    @GetMapping
    public ResponseEntity<List<MerchantCategoryCodeDto>> getMerchantCategoryCodeList() {
        return ResponseEntity.ok(merchantCategoryCodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantCategoryCodeDto> getMerchantCategoryCodeById(@PathVariable("id") Long id) {
        return merchantCategoryCodeService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveMerchantCategoryCode(@RequestBody @Valid MerchantCategoryCodeDto merchantCategoryCodeDto) {
        Optional<MerchantCategoryCodeDto> merchantCategoryCodeOptional = merchantCategoryCodeService.findById(merchantCategoryCodeDto.getId());
        if (merchantCategoryCodeOptional.isPresent()) {
            return ResponseEntity.badRequest().body("MerchantCategoryCode with that parameters is exists");
        } else {
            log.info("Saving merchantCategoryCode with parameters {}", merchantCategoryCodeDto);
            return ResponseEntity.ok(merchantCategoryCodeService.save(merchantCategoryCodeDto));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantCategoryCodeDto> updateMerchantCategoryCode(@PathVariable("id") Long id, @RequestBody @Valid MerchantCategoryCodeDto merchantCategoryCodeDto) {
        return merchantCategoryCodeService.update(id, merchantCategoryCodeDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping("/createTableMerchantCategoryCode")
    public ResponseEntity<String> createTableMerchantCategoryCode() {
        log.info("Creating table merchant category code");
        if (merchantCategoryCodeService.createTable()) {
            return ResponseEntity.ok("Successfully created table MerchantCategoryCode");
        } else {
            return ResponseEntity.badRequest().body("Failed to created table MerchantCategoryCode");
        }
    }


    @PostMapping("/fillTableMerchantCategoryCode")
    public ResponseEntity<String> fillTableMerchantCategoryCode() {
        log.info("Filling table merchant category code");
        createTableMerchantCategoryCode();
        if (merchantCategoryCodeService.initializeTable()) {
            return ResponseEntity.ok("Successfully filled table MerchantCategoryCode");
        } else {
            return ResponseEntity.status(500).body("Failed to fill table MerchantCategoryCode");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMerchantCategoryCode(@PathVariable Long id) {
        return merchantCategoryCodeService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/clearTableMerchantCategoryCode")
    public ResponseEntity<?> clearTableMerchantCategoryCode() {
        log.info("Clearing table merchant category code");
        if (merchantCategoryCodeService.deleteAll()) {
            return ResponseEntity.ok("Successfully dropped table MerchantCategoryCode");
        } else {
            return ResponseEntity.status(500).body("Failed to dropped table MerchantCategoryCode");
        }
    }

    @DeleteMapping("/dropTableMerchantCategoryCode")
    public ResponseEntity<String> dropTableMerchantCategoryCode() {
        log.info("Dropping table merchant category code");
        if (merchantCategoryCodeService.dropTable()) {
            return ResponseEntity.ok("Successfully dropped table MerchantCategoryCode");
        } else {
            return ResponseEntity.status(500).body("Failed to dropped table MerchantCategoryCode");
        }
    }
}
