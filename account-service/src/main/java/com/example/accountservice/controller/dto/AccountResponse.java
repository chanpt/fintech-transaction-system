package com.example.accountservice.controller.dto;

import com.example.accountservice.model.AccountType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AccountResponse {
    
    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private AccountType accountType;
    private Double balance;

public AccountResponse() {}

public AccountResponse(Long id, String accountNumber, String accountHolderName, AccountType accountType, Double balance) {
    this.id = id;
    this.accountNumber = accountNumber;
    this.accountHolderName = accountHolderName;
    this.accountType = accountType;
    this.balance = balance;
}

public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance){
        this.balance = balance;
    }
}