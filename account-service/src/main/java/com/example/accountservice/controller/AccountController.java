package com.example.accountservice.controller;

import com.example.accountservice.controller.dto.AccountRequest;
import com.example.accountservice.controller.dto.AccountResponse;
import com.example.accountservice.model.Account;
import com.example.accountservice.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Account API", description = "Manage accounts: create, view, update, delete")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Operation(summary = "Create a new account")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid account data")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
        @Parameter(description = "Account request payload", required = true)
        @Valid @RequestBody AccountRequest request
    ) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List all accounts")
    @ApiResponse(responseCode = "200", description = "List of account records")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get account by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account found"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
        @Parameter(description = "ID of the account to retrieve", required = true)
        @PathVariable Long id
    ) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update account details by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account updated successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
        @Parameter(description = "ID of the account to update") @PathVariable Long id, 
        @Parameter(description = "Updated account data") @RequestBody Account updatedAccount
    ) {
        Account account = accountService.updateAccount(id, updatedAccount);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Delete account by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Account deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
        @Parameter(description = "ID of the account to delete") @PathVariable Long id
    ) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}