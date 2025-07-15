package com.example.transactionservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateTransaction() throws Exception {
        Transaction trans = new Transaction("J717", "E1141", 512.0, "Transfer");
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(trans);

        mockMvc.perform(post("/transactions")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(trans)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.senderAccountNumber").value("J717"))
               .andExpect(jsonPath("$.receiverAccountNumber").value("E1141"))
               .andExpect(jsonPath("$.amount").value(512.0));
    }

    @Test
    void testCreateTransaction_invalidInput() throws Exception {
        Transaction trans = new Transaction("", "", 0.0, null);

        mockMvc.perform(post("/transactions")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(trans)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllTransactions() throws Exception {
        List<Transaction> transactions = List.of(
            new Transaction("A001", "Z110", 500.0, "Testing1"),
            new Transaction("B001", "Y110", 100.0, "Testing2")
        );
        
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        mockMvc.perform(get("/transactions"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].senderAccountNumber").value("A001"))
               .andExpect(jsonPath("$[1].receiverAccountNumber").value("Y110"))
               .andExpect(jsonPath("$[1].amount").value(100.0));
    }

    @Test 
    void testGetTransactionById_found() throws Exception {
        Transaction trans = new Transaction("J717", "E1141", 512.0, "Transfer");
        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(trans));

        mockMvc.perform(get("/transactions/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.senderAccountNumber").value("J717"))
               .andExpect(jsonPath("$.receiverAccountNumber").value("E1141"));
    }

    @Test
    void testGetTransactionById_notFound() throws Exception {
        when(transactionService.getTransactionById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/transactions/2"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete("/transactions/1"))
               .andExpect(status().isNoContent());
    }
}

