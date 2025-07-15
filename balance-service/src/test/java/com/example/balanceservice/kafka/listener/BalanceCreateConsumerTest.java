package com.example.balanceservice.kafka.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.balanceservice.kafka.event.BalanceCreateEvent;
import com.example.balanceservice.service.BalanceService;

@SpringBootTest
public class BalanceCreateConsumerTest {
    
    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceCreateListener balanceCreateListener;

    @Test
    void testHandleBalanceCreate() {
        BalanceCreateEvent event = new BalanceCreateEvent("A0001", 1000.0);

        balanceCreateListener.handleBalanceCreate(event);

        verify(balanceService, times(1)).createBalanceIfNotExists("A0001", 1000.0);
    }

    @Test
    void testHandleBalanceCreate_nullEvent() {
        assertDoesNotThrow(() -> balanceCreateListener.handleBalanceCreate(null));
        verifyNoInteractions(balanceService);
    }

    @Test
    void testHandleBalanceCreate_nullField() {
        BalanceCreateEvent event = new BalanceCreateEvent(null, null);

        assertDoesNotThrow(() -> balanceCreateListener.handleBalanceCreate(event));
        verifyNoInteractions(balanceService);
    }
}
