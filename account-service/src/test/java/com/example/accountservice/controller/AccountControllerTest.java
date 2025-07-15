package com.example.accountservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.example.accountservice.controller.dto.AccountRequest;
import com.example.accountservice.controller.dto.AccountResponse;
import com.example.accountservice.model.Account;
import com.example.accountservice.model.AccountType;
import com.example.accountservice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAccount() throws Exception {
        AccountRequest request = new AccountRequest("J0717", "Joker", AccountType.CUSTODIAN, 1000.0);
        AccountResponse response = new AccountResponse(1L, "J0717", "Joker", AccountType.CUSTODIAN, 1000.0);

        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/accounts")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.accountHolderName").value("Joker"))
               .andExpect(jsonPath("$.accountType").value("CUSTODIAN"));
    }

    @Test
    void testCreateAccount_invalidInput() throws Exception {
        AccountRequest invalidRequest = new AccountRequest("", "", null, null);

        mockMvc.perform(post("/accounts")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(invalidRequest)))
               .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllAccounts() throws Exception {
        List<Account> accounts = List.of(
            new Account("A001", "John", AccountType.CLEARING, 1000.0),
            new Account("B110","Susan", AccountType.CUSTODIAN, 2000.0)
        );

        when(accountService.getAllAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].accountHolderName").value("John"))
               .andExpect(jsonPath("$[1].balance").value(2000));
    }

    @Test
    void testGetAccountById_found() throws Exception {
        Account account = new Account("J0717", "Joker", AccountType.CUSTODIAN, 1000.0);
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/accounts/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountHolderName").value("Joker"))
               .andExpect(jsonPath("$.accountNumber").value("J0717"));
    }

    @Test
    void testGetAccountById_notFound() throws Exception {
        when(accountService.getAccountById(10L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/accounts/10"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAccount() throws Exception {
        Account updatedAccount = new Account("E1111", "UpdatedUser", AccountType.OTHER, 2000.0);
        when(accountService.updateAccount(eq(1L), any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(put("/accounts/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(updatedAccount)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountHolderName").value("UpdatedUser"));
    }

    @Test
    void testDeleteAccount() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/accounts/1"))
               .andExpect(status().isNoContent());
    }
}