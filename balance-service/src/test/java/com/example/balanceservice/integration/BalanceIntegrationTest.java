package com.example.balanceservice.integration;

import com.example.balanceservice.kafka.event.BalanceCreateEvent;
import com.example.balanceservice.kafka.event.BalanceUpdateEvent;
import com.example.balanceservice.model.Balance;
import com.example.balanceservice.repository.BalanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.errors.InterruptException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InterruptedIOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {
    "balance-create-topic",
    "balance-update-topic"
})
public class BalanceIntegrationTest {

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    ObjectMapper objectMapper;

    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setup() {
        balanceRepository.deleteAll();
        balanceRepository.flush();

        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configs));
    }

    // -------------------------- Service Integration Test -------------------------- 
    @Test
    void testBalanceCreatedViaKafka() throws InterruptedException {
        BalanceCreateEvent event = new BalanceCreateEvent("A001", 500.0);
        kafkaTemplate.send("balance-create-topic", event);

        Thread.sleep(3000); // wait for listner to process

        Balance balance = balanceRepository.findByAccountNumber("A001").orElse(null);
        assertNotNull(balance);
        assertEquals(500.0, balance.getBalance());
    }

    @Test
    void testDuplicateBalanceCreateEvent() throws InterruptedException {
        balanceRepository.save(new Balance("J717", 500.0));

        BalanceCreateEvent invalidEvent = new BalanceCreateEvent("J717", 666.0);
        kafkaTemplate.send("balance-create-topic", invalidEvent);

        Thread.sleep(3000);

        Balance balance = balanceRepository.findByAccountNumber("J717").orElse(null);
        assertNotNull(balance);
        assertEquals(500.0, balance.getBalance());
    }

    @Test
    void testBalanceUpdatedViaKafka() throws InterruptedException {
        balanceRepository.save(new Balance("J717", 1000.0));

        BalanceUpdateEvent event = new BalanceUpdateEvent("J717", 300.0, "DEBIT");
        kafkaTemplate.send("balance-update-topic", event);

        Thread.sleep(1000); 

        Balance updated = balanceRepository.findByAccountNumber("J717").orElse(null);
        assertNotNull(updated);
        assertEquals(700.0, updated.getBalance());
    }

    @Test
    void testInvalidBalanceUpdateEvent() throws InterruptedException {
        balanceRepository.save(new Balance("A001", 100.0));

        BalanceUpdateEvent invalidEvent = new BalanceUpdateEvent(null, 100.0, "CREDIT");
        kafkaTemplate.send("balance-update-topic", invalidEvent);

        Thread.sleep(1000); 

        Balance balance = balanceRepository.findByAccountNumber("A001").orElse(null);
        assertNotNull(balance);
        assertEquals(100.0, balance.getBalance());
    }

    // -------------------------- Controller Integration Test --------------------------
    @Test
    void testGetBalanceByAccountNumber() throws Exception {
        balanceRepository.save(new Balance("B001", 3000.0));

        mockMvc.perform(get("/balance/B001")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.accountNumber").value("B001"))
               .andExpect(jsonPath("$.balance").value(3000.0));
    }

    @Test 
    void testGetBalance_notFound() throws Exception {
        mockMvc.perform(get("/balance/B001")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound());
    }
}