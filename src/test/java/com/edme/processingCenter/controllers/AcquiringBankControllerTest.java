package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.AcquiringBankDto;
import com.edme.processingCenter.services.AcquiringBankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AcquiringBankControllerTest {
    @Mock
    private AcquiringBankService acquiringBankService;

    @InjectMocks
    private AcquiringBankController acquiringBankController;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAcquiringBanks_ReturnsList() {
        List<AcquiringBankDto> mockList = List.of(new AcquiringBankDto(), new AcquiringBankDto());
        when(acquiringBankService.findAll()).thenReturn(mockList);

        ResponseEntity<List<AcquiringBankDto>> response = acquiringBankController.getAllAcquiringBanks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(acquiringBankService).findAll();
    }

    @Test
    void getAcquiringBankById_Found_ReturnsDto() {
        AcquiringBankDto dto = new AcquiringBankDto();
        when(acquiringBankService.findById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.getAcquiringBankById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(acquiringBankService).findById(1L);
    }

    @Test
    void getAcquiringBankById_NotFound_ReturnsNotFound() {
        when(acquiringBankService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.getAcquiringBankById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void saveAcquiringBank_ReturnsSavedDto() {
        AcquiringBankDto dto = new AcquiringBankDto();
        when(acquiringBankService.save(dto)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = acquiringBankController.saveAcquiringBank(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(dto), response.getBody());
    }

    @Test
    void updateAcquiringBank_Successful() {
        AcquiringBankDto dto = new AcquiringBankDto();
        when(acquiringBankService.update(eq(1L), any())).thenReturn(Optional.of(dto));

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.updateAcquiringBank(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateAcquiringBank_Failure() {
        AcquiringBankDto dto = new AcquiringBankDto();
        when(acquiringBankService.update(eq(1L), any())).thenReturn(Optional.empty());

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.updateAcquiringBank(1L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTableAcquiringBank_Success() {
        when(acquiringBankService.createTable()).thenReturn(true);

        ResponseEntity<String> response = acquiringBankController.createTableAcquiringBank();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully created table AcquiringBank", response.getBody());
    }

    @Test
    void createTableAcquiringBank_Failure() {
        when(acquiringBankService.createTable()).thenReturn(false);

        ResponseEntity<String> response = acquiringBankController.createTableAcquiringBank();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to create table AcquiringBank", response.getBody());
    }

    @Test
    void fillTableAcquiringBank_Success() {
        when(acquiringBankService.createTable()).thenReturn(true);
        when(acquiringBankService.initializeTable()).thenReturn(true);

        ResponseEntity<String> response = acquiringBankController.fillTableAcquiringBank();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully filled table AcquiringBank", response.getBody());
    }

    @Test
    void fillTableAcquiringBank_Failure() {
        when(acquiringBankService.createTable()).thenReturn(true);
        when(acquiringBankService.initializeTable()).thenReturn(false);

        ResponseEntity<String> response = acquiringBankController.fillTableAcquiringBank();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to fill table AcquiringBank", response.getBody());
    }

    @Test
    void deleteAcquiringBank_Success() {
        when(acquiringBankService.delete(1L)).thenReturn(true);

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.deleteAcquiringBank(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteAcquiringBank_NotFound() {
        when(acquiringBankService.delete(1L)).thenReturn(false);

        ResponseEntity<AcquiringBankDto> response = acquiringBankController.deleteAcquiringBank(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void clearTableAcquiringBank_Success() {
        when(acquiringBankService.deleteAll()).thenReturn(true);

        ResponseEntity<String> response = acquiringBankController.clearTableAcquiringBank();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully cleared table AcquiringBank", response.getBody());
    }

    @Test
    void clearTableAcquiringBank_Failure() {
        when(acquiringBankService.deleteAll()).thenReturn(false);

        ResponseEntity<String> response = acquiringBankController.clearTableAcquiringBank();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void dropTableAcquiringBank_Success() {
        when(acquiringBankService.dropTable()).thenReturn(true);

        ResponseEntity<String> response = acquiringBankController.dropTableAcquiringBank();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully dropped table AcquiringBank", response.getBody());
    }

    @Test
    void dropTableAcquiringBank_Failure() {
        when(acquiringBankService.dropTable()).thenReturn(false);

        ResponseEntity<String> response = acquiringBankController.dropTableAcquiringBank();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to dropped table AcquiringBank", response.getBody());
    }

}