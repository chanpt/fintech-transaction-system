package com.example.balanceservice.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.balanceservice.kafka.event.BalanceCreateEvent;
import com.example.balanceservice.service.BalanceService;

@Service
public class BalanceCreateListener {
    
    @Autowired
    private BalanceService balanceService;

    @KafkaListener(
        topics = "balance-create-topic",
        groupId = "balance-service-group",
        containerFactory = "balanceCreateKafkaListenerContainerFactory"
    )
    public void consume(@Payload BalanceCreateEvent event) {
        balanceService.createBalanceIfNotExists(event.getAccountNumber(), event.getBalance());
    }
}
