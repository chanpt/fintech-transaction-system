package com.example.balanceservice.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.balanceservice.kafka.event.BalanceUpdateEvent;
import com.example.balanceservice.kafka.event.BalanceCreateEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    
    @Bean
    public ConsumerFactory<String, BalanceCreateEvent> balanceCreateConsumerFactory() {
        JsonDeserializer<BalanceCreateEvent> jsonDeserializer = new JsonDeserializer<>(BalanceCreateEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(false);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "balance-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.balanceservice.kafka.event.BalanceCreateEvent");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); 

        return new DefaultKafkaConsumerFactory<>(props);

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BalanceCreateEvent> balanceCreateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BalanceCreateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(balanceCreateConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BalanceUpdateEvent> balanceUpdateConsumerFactory() {
        JsonDeserializer<BalanceUpdateEvent> jsonDeserializer = new JsonDeserializer<>(BalanceUpdateEvent.class);
        jsonDeserializer.addTrustedPackages("*");
        jsonDeserializer.setUseTypeMapperForKey(false);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "balance-service-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.example.balanceservice.kafka.event.BalanceUpdateEvent");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); 

        return new DefaultKafkaConsumerFactory<>(props);
  }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BalanceUpdateEvent> balanceUpdateKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BalanceUpdateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(balanceUpdateConsumerFactory());
        return factory;
    }
}
