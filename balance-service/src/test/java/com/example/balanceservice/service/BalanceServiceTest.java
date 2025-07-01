package com.example.balanceservice.service;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.repository.BalanceRepository;
import com.example.balanceservice.service.BalanceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.*;

import java.beans.Transient;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceServiceTest {
    
    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalanceByAccountId_found() {
        Balance balance = new Balance(7L,1000.0);

        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.of(balance));

        Optional<Balance> result = balanceService.getBalanceByAccountId(1L);

        assertTrue(result.isPresent());
        assertEquals(1000.0, result.get().getBalance());
        verify(balanceRepository, times(1)).findByAccountId(1L);
    }

    @Test
    void testGetBalanceByAccountId_notFound() {
        when(balanceRepository.findByAccountId(1L)).thenReturn(Optional.empty());

        Optional<Balance> result = balanceService.getBalanceByAccountId(1L);
        
        assertFalse(result.isPresent());
    }
}
