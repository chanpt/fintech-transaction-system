package com.example.accountservice.service;

import com.example.accountservice.controller.dto.AccountRequest;
import com.example.accountservice.controller.dto.AccountResponse;
import com.example.accountservice.kafka.event.BalanceCreateEvent;
import com.example.accountservice.kafka.producer.BalanceCreateProducer;
import com.example.accountservice.model.Account;
import com.example.accountservice.model.AccountType;
import com.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;   // automatically inject a dependency 
import org.springframework.stereotype.Service;                   // Spring will manage the business logic service as a bean 

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BalanceCreateProducer balanceCreateProducer;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountHolderName(request.getAccountHolderName());
        account.setAccountType(request.getAccountType() != null ? request.getAccountType() : AccountType.OTHER);
        account.setBalance(request.getBalance() != null ? request.getBalance() : 0.0);

        Account savedAccount = accountRepository.save(account);

        // send Kafka event
        BalanceCreateEvent event = new BalanceCreateEvent(savedAccount.getAccountNumber(), savedAccount.getBalance());
        balanceCreateProducer.sendBalanceCreate(event);

        return mapToResponse(savedAccount);
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        return response;
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        return accountRepository.findById(id).map(account -> {
            account.setAccountNumber(updatedAccount.getAccountNumber());
            account.setAccountHolderName(updatedAccount.getAccountHolderName());
            account.setBalance(updatedAccount.getBalance());
            return accountRepository.save(account);
        }).orElseGet(() -> {
            updatedAccount.setId(id);
            return accountRepository.save(updatedAccount);
        });
    }
}
