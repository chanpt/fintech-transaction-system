package com.example.transactionservice.service;

import com.example.transactionservice.kafka.event.BalanceUpdateEvent;
import com.example.transactionservice.kafka.producer.BalanceUpdateProducer;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.StackWalker.Option;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private TransactionService transactionService;  

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BalanceUpdateProducer balanceUpdateProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(transactionRepository, balanceUpdateProducer);
    }

    @Test
    void testGetAllTransactions() {
        Transaction trans1 = new Transaction("A001", "Z110", 500.0, "Testing1");
        Transaction trans2 = new Transaction("B001", "Y110", 100.0, "Testing2");
        
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(trans1, trans2));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }
    
    @Test
    void testGetTransactionById_found() {
        Transaction trans = new Transaction("0717", "1114", 1717.0, "Daily Expenses");

        when(transactionRepository.findById(2L)).thenReturn(Optional.of(trans));

        Optional<Transaction> result = transactionService.getTransactionById(2L);

        assertTrue(result.isPresent());
        assertEquals(1717, result.get().getAmount());
    }

    @Test
    void testGetTransactionById_notFound() {
        when(transactionRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Transaction> result = transactionService.getTransactionById(3L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateTransaction() {
        Transaction trans = new Transaction("J717", "E1141", 512.0, "Transfer");
        when(transactionRepository.save(trans)).thenReturn(trans);

        Transaction result = transactionService.createTransaction(trans);

        assertEquals("J717", result.getSenderAccountNumber());
        assertEquals("E1141", result.getReceiverAccountNumber());
        assertEquals(512, result.getAmount());
        verify(transactionRepository, times(1)).save(trans);
        verify(balanceUpdateProducer, times(2)).sendBalanceUpdate(any(BalanceUpdateEvent.class));
    }

    @Test
    void testDeleteAccount() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionRepository.deleteById(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }
}
