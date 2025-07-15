package com.example.balanceservice.kafka.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.balanceservice.kafka.event.BalanceUpdateEvent;
import com.example.balanceservice.service.BalanceService;

@SpringBootTest
public class BalanceUpdateConsumerTest {
    
    @Mock
    private BalanceService balanceService;

    @InjectMocks
    private BalanceUpdateListener balanceUpdateListener;

    @Test
    void testHandleBalanceUpdate() {
        BalanceUpdateEvent event = new BalanceUpdateEvent("J0717", 500.0, "CREDIT");

        balanceUpdateListener.handleBalanceUpdate(event);

        verify(balanceService, times(1)).updateBalance("J0717", 500.0, "CREDIT");
    }

    @Test
    void testHandleBalanceUpdate_nullEvent() {
        assertDoesNotThrow(() -> balanceUpdateListener.handleBalanceUpdate(null));
        verifyNoInteractions(balanceService);
    }

    @Test
    void testHandleBalanceUpdate_nullFields() {
        BalanceUpdateEvent event = new BalanceUpdateEvent(null, null, null);

        assertDoesNotThrow(() -> balanceUpdateListener.handleBalanceUpdate(event));
        verifyNoInteractions(balanceService);
    }
}
