package com.example.balanceservice.model;

import jakarta.persistence.*;

@Entity
public class Balance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;
    private Double balance;

    // Constructors
    public Balance() {};

    public Balance(Long accountId, Double balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId() {
        this.accountId = accountId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance() {
        this.balance = balance;
    }

}
