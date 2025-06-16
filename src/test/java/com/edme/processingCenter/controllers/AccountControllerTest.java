package com.edme.processingCenter.controllers;

import com.edme.processingCenter.dto.AccountDto;
import com.edme.processingCenter.services.AccountService;
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

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AutoCloseable closeable;

    //openMocks(this) возвращает AutoCloseable, который можно закрыть вручную lkz bp,t;fybz entxtr.
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void getAllAccounts_ReturnsList() {
        List<AccountDto> accounts = List.of(new AccountDto(), new AccountDto());
        when(accountService.findAll()).thenReturn(accounts);

        ResponseEntity<List<AccountDto>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(accountService).findAll();
    }

    @Test
    void getAccountById_Found_ReturnsDto() {
        AccountDto dto = new AccountDto();
        when(accountService.findById(1L)).thenReturn(Optional.of(dto));

        ResponseEntity<AccountDto> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getAccountById_NotFound_Returns404() {
        when(accountService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<AccountDto> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void saveAccount_ReturnsSavedDto() {
        AccountDto dto = new AccountDto();
        when(accountService.save(dto)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = accountController.saveAccount(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(dto), response.getBody());
    }

    @Test
    void updateAccount_Success_ReturnsUpdatedDto() {
        AccountDto dto = new AccountDto();
        when(accountService.update(1L, dto)).thenReturn(Optional.of(dto));

        ResponseEntity<?> response = accountController.updateAccount(1L, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateAccount_Failure_Returns400() {
        AccountDto dto = new AccountDto();
        when(accountService.update(1L, dto)).thenReturn(Optional.empty());

        ResponseEntity<?> response = accountController.updateAccount(1L, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTableAccounts_Success_ReturnsOk() {
        when(accountService.createTable()).thenReturn(true);

        ResponseEntity<?> response = accountController.createTableAccounts(List.of());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully created table accounts", response.getBody());
    }

    @Test
    void createTableAccounts_Failure_ReturnsBadRequest() {
        when(accountService.createTable()).thenReturn(false);

        ResponseEntity<?> response = accountController.createTableAccounts(List.of());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed to create table accounts", response.getBody());
    }

    @Test
    void fillTableAccounts_Success_ReturnsOk() {
        when(accountService.initializeTable()).thenReturn(true);

        ResponseEntity<String> response = accountController.fillTableAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully initialized table Accounts", response.getBody());
    }

    @Test
    void fillTableAccounts_Failure_Returns500() {
        when(accountService.initializeTable()).thenReturn(false);

        ResponseEntity<String> response = accountController.fillTableAccounts();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to initialize table Accounts", response.getBody());
    }

    @Test
    void deleteAccount_Success_Returns204() {
        when(accountService.delete(1L)).thenReturn(true);

        ResponseEntity<String> response = accountController.deleteAccount(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteAccount_Failure_Returns400() {
        when(accountService.delete(1L)).thenReturn(false);

        ResponseEntity<String> response = accountController.deleteAccount(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void clearTableAccounts_Success_ReturnsOk() {
        when(accountService.initializeTable()).thenReturn(true);

        ResponseEntity<String> response = accountController.clearTableAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully cleared table Accounts", response.getBody());
    }

    @Test
    void clearTableAccounts_Failure_Returns500() {
        when(accountService.initializeTable()).thenReturn(false);

        ResponseEntity<String> response = accountController.clearTableAccounts();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to clear table Accounts", response.getBody());
    }

    @Test
    void dropTableAccess_Success_ReturnsOk() {
        when(accountService.initializeTable()).thenReturn(true);

        ResponseEntity<String> response = accountController.dropTableAccess();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully dropped table access", response.getBody());
    }

    @Test
    void dropTableAccess_Failure_Returns500() {
        when(accountService.initializeTable()).thenReturn(false);

        ResponseEntity<String> response = accountController.dropTableAccess();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to drop table access", response.getBody());
    }
}