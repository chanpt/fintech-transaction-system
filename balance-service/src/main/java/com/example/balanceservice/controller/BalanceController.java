package com.example.balanceservice.controller;

import com.example.balanceservice.model.Balance;
import com.example.balanceservice.service.BalanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/balance")
@Tag(name = "Balance API", description = "Operations related to account balances")
public class BalanceController {
    
    @Autowired 
    private BalanceService balanceService;

    @Operation(summary = "Get account balance by account number")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance found"),
        @ApiResponse(responseCode = "404", description = "Balance not found for the given account number")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Balance> getBalance(
        @Parameter(description = "Account number to get the balance")
        @PathVariable String accountNumber
    ) {
        Optional<Balance> balance = balanceService.getBalanceByAccountNumber(accountNumber);
        
        return balance.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
}
