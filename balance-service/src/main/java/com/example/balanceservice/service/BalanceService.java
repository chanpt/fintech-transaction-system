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

    public Optional<Balance> getBalanceByAccountId(Long accountId) {
        return balanceRepository.findByAccountId(accountId);
    }
}
