package com.example.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// This class will be mapped to a table in the database
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String accountHolderName;
    private Double balance;

    // Constructors
    public Account() {}

    public Account(String accountNumber, String accountHolderName, Double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }
}
