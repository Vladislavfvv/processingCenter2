package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.CardStatusDto;
import com.edme.processingCenter.services.CardStatusService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cardStatuses")
public class CardStatusController {

    private CardStatusService cardStatusService;

    @GetMapping
    public ResponseEntity<List<CardStatusDto>> getListCardStatuses() {
        return ResponseEntity.ok(cardStatusService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardStatusDto> getCardStatusById(Long id) {
        return cardStatusService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveCardStatus(@RequestBody @Valid CardStatusDto cardStatusDto) {
        log.debug("Saving CardStatus : {}", cardStatusDto);
        return ResponseEntity.ok(cardStatusService.save(cardStatusDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardStatusDto> updateCardStatus(@PathVariable Long id, @RequestBody @Valid CardStatusDto cardStatusDto) {
        log.debug("Updating CardStatus : {}", cardStatusDto);
        return cardStatusService.update(id, cardStatusDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createTableCardStatus")
    public ResponseEntity<String> createTableCardStatus() {
        log.debug("Creating table card status table");
        return cardStatusService.createTable()
                ? ResponseEntity.ok("Creating table card status table")
                : ResponseEntity.badRequest().body("Failed creating table cardStatus");
    }

    @PostMapping("/fillTableCardStatus")
    public ResponseEntity<String> fillTableCardStatus() {
        log.debug("Filling table card status table");
        return cardStatusService.initializeTable()
                ? ResponseEntity.ok("Filling table card status table")
                : ResponseEntity.badRequest().body("Failed filling table cardStatus");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCardStatus(@PathVariable Long id) {
        log.debug("Deleting CardStatus : {}", id);
        return cardStatusService.delete(id)
                ? ResponseEntity.ok("Successfull deleting CardStatus ")
                : ResponseEntity.badRequest().body("Failed deleting CardStatus ");
    }

    @DeleteMapping("/clearTableCardStatus")
    public ResponseEntity<String> clearTableCardStatus() {
        log.debug("Clearing table card status table");
        return cardStatusService.deleteAll()
                ? ResponseEntity.ok("Successfully clearing table card status table")
                : ResponseEntity.badRequest().body("Failed clearing table cardStatus");
    }

    @DeleteMapping("/dropTableCardStatus")
    public ResponseEntity<String> dropTableCardStatus() {
        log.debug("Dropping table card status table");
        return cardStatusService.dropTable()
                ? ResponseEntity.ok("Successfully dropping table card status table")
                : ResponseEntity.badRequest().body("Failed dropping table cardStatus");
    }
}
