package com.example.balanceservice.service;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service; 
import java.util.Optional;

@Service
public class BalanceService {
    
    @Autowired
    private BalanceRepository balanceRepository;

    public Optional<Balance> getBalanceByAccountNumber(String accountNumber) {
        return balanceRepository.findByAccountNumber(accountNumber);
    }

    public void createBalanceIfNotExists(String accountNumber, Double initialBalance) {
        if (balanceRepository.findByAccountNumber(accountNumber).isEmpty()) {
            Balance balance = new Balance();
            balance.setAccountNumber(accountNumber);
            balance.setBalance(initialBalance);
            balanceRepository.save(balance);
        }
    }

    public void updateBalance(String accountNumber, Double amount, String type) {
        Optional<Balance> balanceOpt = balanceRepository.findByAccountNumber(accountNumber);
        Balance balance = balanceOpt.orElseGet(() -> {
            Balance newBalance = new Balance();
            newBalance.setAccountNumber(accountNumber);
            newBalance.setBalance(0.0);
            return newBalance;
        });

        if(type.equalsIgnoreCase("DEBIT")) {
            balance.setBalance(balance.getBalance() - amount);
        } else if(type.equalsIgnoreCase("CREDIT")) {
            balance.setBalance(balance.getBalance() + amount);
        }

        balanceRepository.save(balance);
    }
}
