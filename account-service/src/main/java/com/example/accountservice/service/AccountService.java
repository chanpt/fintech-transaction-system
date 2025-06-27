package com.example.accountservice.service;

import com.example.accountservice.model.Account;
import com.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;   // automatically inject a dependency 
import org.springframework.stereotype.Service;                   // Spring will manage the business logic service as a bean 

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
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
