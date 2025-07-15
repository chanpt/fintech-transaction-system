package com.example.balanceservice.kafka.listener;

import com.example.balanceservice.kafka.event.BalanceUpdateEvent;
import com.example.balanceservice.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BalanceUpdateListener {
    
    @Autowired
    private BalanceService balanceService;

    @KafkaListener(
        topics = "balance-update-topic", 
        groupId = "balance-service-group", 
        containerFactory = "balanceUpdateKafkaListenerContainerFactory"
    )
    public void handleBalanceUpdate(@Payload BalanceUpdateEvent event) {
        if (event == null || event.getAccountNumber() == null || event.getAmount() == null || event.getType() == null) {
            log.warn("Invalid BalanceUpdateEvent: {}", event);
            return;
        }
        balanceService.updateBalance(event.getAccountNumber(), event.getAmount(), event.getType());
    }

    
}
