package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.TransactionTypeDto;
import com.edme.processingCenter.services.TransactionTypeService;
import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionTypeControllerTest {
    @Mock
    private TransactionTypeService transactionTypeService;

    @InjectMocks
    private TransactionTypeController controller;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllTransactionTypes_ReturnsList() {
        List<TransactionTypeDto> mockList = List.of(new TransactionTypeDto(), new TransactionTypeDto());
        when(transactionTypeService.findAll()).thenReturn(mockList);

        ResponseEntity<List<TransactionTypeDto>> response = controller.getAllTransactionTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(transactionTypeService).findAll();
    }

    @Test
    void getTransactionTypeById_Found_ReturnsDto() {
        TransactionTypeDto dto = new TransactionTypeDto();
        when(transactionTypeService.findById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<TransactionTypeDto> response = controller.getTransactionTypeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getTransactionTypeById_NotFound_Returns404() {
        when(transactionTypeService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<TransactionTypeDto> response = controller.getTransactionTypeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

@Test
void saveTransactionType_ReturnsSavedDto() {
    TransactionTypeDto dto = new TransactionTypeDto();
    when(transactionTypeService.save(dto)).thenReturn(Optional.of(dto));

    ResponseEntity<?> response = controller.saveTransactionType(dto);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertEquals(Optional.of(dto), response.getBody());
}


    @Test
    void updateTransactionType_Success_ReturnsUpdatedDto() {
        TransactionTypeDto dto = new TransactionTypeDto();
        when(transactionTypeService.update(1L, dto)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = controller.updateTransactionType(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateTransactionType_Failure_Returns400() {
        TransactionTypeDto dto = new TransactionTypeDto();
        when(transactionTypeService.update(1L, dto)).thenReturn(Optional.empty());

        ResponseEntity<?> response = controller.updateTransactionType(1L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTableTransactionType_Success() {
        when(transactionTypeService.createTable()).thenReturn(true);

        ResponseEntity<String> response = controller.createTableTransactionType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully created table TransactionType.", response.getBody());
    }

    @Test
    void createTableTransactionType_Failure() {
        when(transactionTypeService.createTable()).thenReturn(false);

        ResponseEntity<String> response = controller.createTableTransactionType();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to create table TransactionType.", response.getBody());
    }

    @Test
    void fillTableTransactionType_Success() {
        when(transactionTypeService.createTable()).thenReturn(true);
        when(transactionTypeService.initializeTable()).thenReturn(true);

        ResponseEntity<String> response = controller.fillTableTransactionType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully filled table TransactionType.", response.getBody());
    }

    @Test
    void fillTableTransactionType_Failure() {
        when(transactionTypeService.createTable()).thenReturn(true);
        when(transactionTypeService.initializeTable()).thenReturn(false);

        ResponseEntity<String> response = controller.fillTableTransactionType();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to fill table TransactionType.", response.getBody());
    }

    @Test
    void deleteTransactionType_Success() {
        when(transactionTypeService.delete(1L)).thenReturn(true);

        ResponseEntity<TransactionTypeDto> response = controller.deleteTransactionType(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteTransactionType_Failure() {
        when(transactionTypeService.delete(1L)).thenReturn(false);

        ResponseEntity<TransactionTypeDto> response = controller.deleteTransactionType(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void clearTableTransactionType_Success() {
        when(transactionTypeService.deleteAll()).thenReturn(true);

        ResponseEntity<String> response = controller.clearTableTransactionType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully cleared table TransactionType.", response.getBody());
    }

    @Test
    void clearTableTransactionType_Failure() {
        when(transactionTypeService.deleteAll()).thenReturn(false);

        ResponseEntity<String> response = controller.clearTableTransactionType();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to clear table TransactionType.", response.getBody());
    }

    @Test
    void dropTableTransactionType_Success() {
        when(transactionTypeService.dropTable()).thenReturn(true);

        ResponseEntity<String> response = controller.dropTableTransactionType();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully dropped table TransactionType.", response.getBody());
    }

    @Test
    void dropTableTransactionType_Failure() {
        when(transactionTypeService.dropTable()).thenReturn(false);

        ResponseEntity<String> response = controller.dropTableTransactionType();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to dropped table TransactionType.", response.getBody());
    }
}