package com.example.transactionservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI transactionServicOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Transaction Service API")
                .version("1.0.0")
                .description("Handles transaction creation, status, and Kafka event publishing")
                .contact(new Contact()
                    .name("Chan Pei Ting")
                    .url("https://github.com/chanpt")
                )
            );   
    }
}
