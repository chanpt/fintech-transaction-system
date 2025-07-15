package com.example.accountservice.integration;

import com.example.accountservice.controller.dto.AccountRequest;
import com.example.accountservice.controller.dto.AccountResponse;
import com.example.accountservice.model.Account;
import com.example.accountservice.model.AccountType;
import com.example.accountservice.repository.AccountRepository;
import com.example.accountservice.service.AccountService;
import com.example.accountservice.kafka.producer.BalanceCreateProducer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountIntegrationTest {
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BalanceCreateProducer balanceCreateProducer;

    @BeforeEach
    void cleanUp() {
        accountRepository.deleteAll();
        reset(balanceCreateProducer);
    }

    // -------------------------- Service Integration Test -------------------------- 
    @Test
    void testService_createAccount() {
        AccountRequest request = new AccountRequest("A100", "Alice", AccountType.OTHER, 1000.0);
        var response = accountService.createAccount(request);

        assertNotNull(response.getId());
        assertEquals("A100", response.getAccountNumber());
        assertEquals("Alice", response.getAccountHolderName());
        assertEquals(1000.0, response.getBalance());

        verify(balanceCreateProducer, times(1)).sendBalanceCreate(any());
    }

    @Test
    void testService_createAccount_missingFields() {
        AccountRequest badRequest = new AccountRequest(null, null, null, 110.0);

        Exception exception = assertThrows(Exception.class, () -> {
            accountService.createAccount(badRequest);
        });

        assertNotNull(exception.getMessage());
        verify(balanceCreateProducer, never()).sendBalanceCreate(any());
    }

    @Test
    void testService_createDuplicateAccountNumber() {
        AccountRequest request = new AccountRequest("A001", "Alice", AccountType.BROKERAGE, 200.0);
        accountService.createAccount(request); // first insert

        AccountRequest duplicate = new AccountRequest("A001", "Bob", AccountType.OTHER, 500.0);

        Exception exception = assertThrows(Exception.class, () -> {
            accountService.createAccount(duplicate); // second insert should fail
        });

        String message = exception.getMessage();
        assertTrue(message.contains("already exists"));
        verify(balanceCreateProducer, times(1)).sendBalanceCreate(any()); // only first call
    }

    @Test
    void testService_createAccount_kafkaProducerFailes() {
        AccountRequest request = new AccountRequest("A001", "Alice", AccountType.BROKERAGE, 200.0);

        doThrow(new RuntimeException("Kafka down")).when(balanceCreateProducer).sendBalanceCreate(any());

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals("A001", response.getAccountNumber());

        verify(balanceCreateProducer, times(1)).sendBalanceCreate(any());
    }

    // -------------------------- Controller Integration Test --------------------------
    @Test
    void testController_createAccount() throws Exception {
        AccountRequest request = new AccountRequest("A101", "Bob", AccountType.BROKERAGE, 500.0);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("A101"))
                .andExpect(jsonPath("$.accountHolderName").value("Bob"));

        verify(balanceCreateProducer, times(1)).sendBalanceCreate(any());
    }

    @Test
    void testController_createAccount_emptyAccountNumber() throws Exception {
        AccountRequest invalid = new AccountRequest("", "John", AccountType.CLEARING, 1000.0);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testController_createAccount_invalidPayload() throws Exception {
        String invalidJson = """
        {
            "accountHolderName": "John"
        }
        """;

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testController_createAccount_nameTooLong() throws Exception {
        String longName = "A".repeat(101);
        AccountRequest invalid = new AccountRequest("A002", longName, AccountType.CUSTODIAN, 500.0);

        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}