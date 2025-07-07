package com.example.balanceservice.kafka.event; 

public class BalanceCreateEvent {
    
    private String accountNumber;
    private Double balance;

    // Constructors
    public BalanceCreateEvent() {}

    public BalanceCreateEvent(String accountNumber, Double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
