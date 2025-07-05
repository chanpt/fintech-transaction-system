package com.example.transactionservice.model;

import org.apache.kafka.common.protocol.types.Field.Str;

import jakarta.persistence.*;

@Entity
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private Double amount;
    private String description;

    //Constructors
    public Transaction() {};

    public Transaction(String senderAccountNumber, String receiverAccountNumber, Double amount, String description) {
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
}
