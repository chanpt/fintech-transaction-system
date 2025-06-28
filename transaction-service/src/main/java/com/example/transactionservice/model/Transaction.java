package com.example.transactionservice.model;

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

    public void setSenderAccountNumber() {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber() {
        this.senderAccountNumber = senderAccountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount() {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription() {
        this.description = description;
    }

    
}
