package com.example.transactionservice.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.transactionservice.kafka.producer.BalanceUpdateProducer;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import com.example.transactionservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionIntegrationTest {
    
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceUpdateProducer balanceUpdateProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanUp() {
        transactionRepository.deleteAll();
        reset(balanceUpdateProducer);
    }

    // -------------------------- Service Integration Test -------------------------- 
    @Test
    void testService_createTransaction() {
        Transaction transaction = new Transaction("A001", "B001", 100.0, "Transfer");

        Transaction saved = transactionService.createTransaction(transaction);

        assertNotNull(saved.getId());
        assertEquals("A001", saved.getSenderAccountNumber());
        assertEquals("B001", saved.getReceiverAccountNumber());
        assertEquals(100.0, saved.getAmount());

        verify(balanceUpdateProducer, times(2)).sendBalanceUpdate(any());
    }

    @Test
    void testService_createTransaction_missingReceiver() {
        Transaction transaction = new Transaction("A001", null, 200.0, "Transfer");

        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertNotNull(exception.getMessage());
        verify(balanceUpdateProducer, never()).sendBalanceUpdate(any());
    }

    @Test
    void testService_createTransaction_zeroAmount() {
        Transaction transaction = new Transaction("A001", "B001", 0.0, "Transfer");

        Exception exception = assertThrows(Exception.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertNotNull(exception.getMessage());
        verify(balanceUpdateProducer, never()).sendBalanceUpdate(any());
    }

    // -------------------------- Controller Integration Test --------------------------
    @Test
    void testController_createTransaction() throws Exception{
        Transaction transaction = new Transaction("A001", "B001", 100.0, "Transfer");

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.senderAccountNumber").value("A001"))
                .andExpect(jsonPath("$.receiverAccountNumber").value("B001"))
                .andExpect(jsonPath("$.amount").value(100.0));

        verify(balanceUpdateProducer, times(2)).sendBalanceUpdate(any());
    }

    @Test
    void testController_createTransaction_invalidPayload() throws Exception {
        String invalidJson = """
            {
              "senderAccountNumber": "A123",
              "amount": 100.0,
              "transactionType": "Transfer"
            }
            """;

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testController_createTransaction_emptySender() throws Exception {
        Transaction transaction = new Transaction("", "B001", 100.0, "Transfer");

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testController_createTransaction_negativeAmount() throws Exception {
        Transaction transaction = new Transaction("A001", "B001", -200.0, "Transfer");

        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }
}
