package com.example.balanceservice.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.service.BalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(BalanceController.class)
public class BalanceControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetBalance_success() throws Exception {
        Balance balance = new Balance("J0717", 500.0);
        when(balanceService.getBalanceByAccountNumber("J0717")).thenReturn(Optional.of(balance));

        mockMvc.perform(get("/balance/J0717"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountNumber").value("J0717"))
               .andExpect(jsonPath("$.balance").value(500.0));
    }

    @Test
    void testGetBalance_notFound() throws Exception {
        when(balanceService.getBalanceByAccountNumber(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/balance/UNKNOWN"))
               .andExpect(status().isNotFound());
    }
}
