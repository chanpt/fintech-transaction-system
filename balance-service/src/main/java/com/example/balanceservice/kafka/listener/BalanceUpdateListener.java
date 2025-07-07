package com.example.balanceservice.kafka.listener;

import com.example.balanceservice.kafka.event.BalanceUpdateEvent;
import com.example.balanceservice.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class BalanceUpdateListener {
    
    @Autowired
    private BalanceService balanceService;

    @KafkaListener(
        topics = "balance-update-topic", 
        groupId = "balance-service-group", 
        containerFactory = "balanceUpdateKafkaListenerContainerFactory"
    )
    public void consume(@Payload BalanceUpdateEvent event) {
        balanceService.updateBalance(event.getAccountNumber(), event.getAmount(), event.getType());
    }

    
}
