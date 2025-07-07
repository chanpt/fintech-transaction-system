package com.example.accountservice.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.example.accountservice.kafka.event.BalanceCreateEvent;


@Service
public class BalanceCreateProducer {
    
    @Autowired
    private KafkaTemplate<String, BalanceCreateEvent> kafkaTemplate;

    public void sendBalanceCreate(BalanceCreateEvent event) {
        kafkaTemplate.send("balance-create-topic", event);
    }
}
