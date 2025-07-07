package com.example.accountservice.service;

import com.example.accountservice.kafka.event.BalanceCreateEvent;
import com.example.accountservice.kafka.producer.BalanceCreateProducer;
import com.example.accountservice.model.Account;
import com.example.accountservice.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BalanceCreateProducer balanceCreateProducer;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        Account acc = new Account("A110", "John",1000.0);
        when(accountRepository.save(any(Account.class))).thenReturn(acc);

        Account result = accountService.createAccount(acc);

        assertEquals("John", result.getAccountHolderName());
        assertEquals(1000, result.getBalance());
        verify(accountRepository, times(1)).save(acc);
        verify(balanceCreateProducer, times(1)).sendBalanceCreate(any(BalanceCreateEvent.class));
    }

    @Test
    void testGetAllAccounts() {
        Account acc1 = new Account("A001", "John", 1000.0);
        Account acc2 = new Account("B110","Susan", 2000.0);
        when(accountRepository.findAll()).thenReturn(Arrays.asList(acc1, acc2));

        List<Account> accounts = accountService.getAllAccounts();

        assertEquals(2, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testGetAccountById_found() {
        Account acc = new Account("J717", "Jacky", 1717.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(acc));

        Optional<Account> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent());
        assertEquals("Jacky", result.get().getAccountHolderName());
        verify(accountRepository,times(1)).findById(1L);
    }

    @Test
    void testGetAccountById_notFound() {
        when(accountRepository.findById(7L)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.getAccountById(7L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteAccount() {
        doNothing().when(accountRepository).deleteById(1L);
        
        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateAccount_existing() {
        Account existing = new Account("A001", "John", 1000.0);
        existing.setId(1L);
        Account updated = new Account("J717", "Jacky", 1717.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account result = accountService.updateAccount(1L, updated);

        assertEquals("J717", result.getAccountNumber());
        assertEquals("Jacky", result.getAccountHolderName());
        assertEquals(1717.0, result.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(existing);
    }

    @Test
    void testUpdateAccount_notExisting() {
        Account updated = new Account("J717", "Jacky", 1717.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        Account result = accountService.updateAccount(1L, updated);

        assertEquals("J717", result.getAccountNumber());
        assertEquals("Jacky", result.getAccountHolderName());
        assertEquals(1717.0, result.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(updated);
    }
}
