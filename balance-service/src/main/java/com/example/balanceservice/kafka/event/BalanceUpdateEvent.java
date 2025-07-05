package com.example.balanceservice.kafka.event;

public class BalanceUpdateEvent {
    
    private String accountNumber;
    private Double amount;
    private String type;  // DEBIT or CREDIT

    // Constructors
    public BalanceUpdateEvent() {}

    public BalanceUpdateEvent(String accountNumber, Double amount, String type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
