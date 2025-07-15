package com.example.transactionservice.controller;

import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction API", description = "Operations related to transactions")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Get all transactions")
    @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Get transaction by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction found"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
        @Parameter(description = "ID of the transaction to retrieve", required = true)
        @PathVariable Long id
    ) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new transaction")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transaction created successful"),
        @ApiResponse(responseCode = "400", description = "Invalid transaction data")
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
        @Parameter(description = "Transaction request payload", required = true)
        @Valid @RequestBody Transaction transaction
    ) {
        Transaction savedTransaction = transactionService.createTransaction(transaction);  
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete transaction by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
        @Parameter(description = "ID of the transaction to delete")
        @PathVariable Long id
    ) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
