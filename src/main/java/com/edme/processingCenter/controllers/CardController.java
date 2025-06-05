package com.edme.processingCenter.controllers;


import com.edme.processingCenter.dto.CardDto;
import com.edme.processingCenter.services.CardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardDto>> getListAllCards() {
        return ResponseEntity.ok(cardService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CardDto> getCardById(@PathVariable("id") Long id) {
        return cardService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveCard(@RequestBody @Valid CardDto cardDto) {
        log.debug("Saving Card: {}", cardDto);
        return ResponseEntity.ok(cardService.save(cardDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long id, @RequestBody @Valid CardDto cardDto) {
        log.debug("Updating Card: {}", cardDto);
        return cardService.update(id, cardDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/createTableCards")
    public ResponseEntity<String> createTableCards() {
        log.debug("Creating table cards");
        return cardService.createTable()
                ? ResponseEntity.ok("Creating table cards")
                : ResponseEntity.badRequest().body("Failed to create table cards");
    }

    @PostMapping("fillTableCards")
    public ResponseEntity<String> fillTableCards() {
        log.debug("Filling table cards");
        return cardService.initializeTable()
                ? ResponseEntity.ok("Filling table cards")
                : ResponseEntity.badRequest().body("Failed to fill table cards");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(@PathVariable Long id) {
        log.debug("Deleting Card: {}", id);
        return cardService.delete(id)
                ? ResponseEntity.ok("Deleted Card")
                : ResponseEntity.badRequest().body("Failed to delete Card");
    }

    @DeleteMapping("/clearTableCards")
    public ResponseEntity<String> clearTableCards() {
        log.debug("Clearing table cards");
        return cardService.deleteAll()
                ? ResponseEntity.ok("Clearing table cards")
                : ResponseEntity.badRequest().body("Failed to clear table cards");
    }

    @DeleteMapping("/dropTableCards")
    public ResponseEntity<String> dropTableCards() {
        log.debug("Dropping table cards");
        return cardService.dropTable()
                ? ResponseEntity.ok("Dropping table cards")
                : ResponseEntity.badRequest().body("Failed to drop table cards");
    }
}

