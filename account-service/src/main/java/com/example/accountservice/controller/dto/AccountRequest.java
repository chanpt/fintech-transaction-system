package com.example.accountservice.controller.dto;

import com.example.accountservice.model.AccountType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AccountRequest {
    
    @NotNull(message = "Account number is required")
    @Size(min = 1, max = 50, message = "Account number must be between 1 and 50 characters")
    private String accountNumber;

    @NotNull(message = "Account holder name is required")
    @Size(min = 1, max = 100, message = "Account number must be between 1 and 100 characters")
    private String accountHolderName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    private Double balance;

    public AccountRequest() {}

    public AccountRequest(String accountNumber, String accountHolderName, AccountType accountType, Double balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.balance = balance;
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
