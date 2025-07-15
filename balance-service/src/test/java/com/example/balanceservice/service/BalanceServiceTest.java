package com.example.balanceservice.service;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.repository.BalanceRepository;
import com.example.balanceservice.service.BalanceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.*;

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
    void testGetBalanceByAccountNumber_found() {
        Balance balance = new Balance("717",1000.0);

        when(balanceRepository.findByAccountNumber("717")).thenReturn(Optional.of(balance));

        Optional<Balance> result = balanceService.getBalanceByAccountNumber("717");

        assertTrue(result.isPresent());
        assertEquals(1000.0, result.get().getBalance());
        verify(balanceRepository, times(1)).findByAccountNumber("717");
    }

    @Test
    void testGetBalanceByAccountNumber_notFound() {
        when(balanceRepository.findByAccountNumber("717")).thenReturn(Optional.empty());

        Optional<Balance> result = balanceService.getBalanceByAccountNumber("717");
        
        assertFalse(result.isPresent());
    }
}
