package com.example.balanceservice.controller;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    
    @Autowired 
    private BalanceService balanceService;

    @GetMapping("/{accountId}")
    public ResponseEntity<Balance> getBalance(@PathVariable Long accountId) {
        Optional<Balance> balance = balanceService.getBalanceByAccountId(accountId);
        return balance.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
}
