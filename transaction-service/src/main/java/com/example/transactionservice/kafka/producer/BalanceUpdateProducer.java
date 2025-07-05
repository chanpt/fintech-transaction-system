package com.example.transactionservice.kafka.producer;

import com.example.transactionservice.kafka.event.BalanceUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BalanceUpdateProducer {
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendBalanceUpdate(BalanceUpdateEvent event) {
        kafkaTemplate.send("balance-update-topic", event);
    }
}
