package com.example.balanceservice.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import com.example.balanceservice.kafka.event.BalanceCreateEvent;
import com.example.balanceservice.service.BalanceService;

@Slf4j
@Service
public class BalanceCreateListener {
    
    @Autowired
    private BalanceService balanceService;

    @KafkaListener(
        topics = "balance-create-topic",
        groupId = "balance-service-group",
        containerFactory = "balanceCreateKafkaListenerContainerFactory"
    )
    public void handleBalanceCreate(@Payload BalanceCreateEvent event) {
        if (event == null || event.getAccountNumber() == null || event.getBalance() == null) {
            log.warn("Invalid BalanceCreateEvent: {}", event);
            return;
        }
        balanceService.createBalanceIfNotExists(event.getAccountNumber(), event.getBalance());
    }
}
