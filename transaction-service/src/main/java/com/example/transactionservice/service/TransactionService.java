package com.example.transactionservice.service;

import com.example.transactionservice.kafka.event.BalanceUpdateEvent;
import com.example.transactionservice.kafka.producer.BalanceUpdateProducer;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final BalanceUpdateProducer balanceUpdateProducer;

    public TransactionService(TransactionRepository transactionRepository,
                              BalanceUpdateProducer balanceUpdateProducer) {
        this.transactionRepository = transactionRepository;
        this.balanceUpdateProducer = balanceUpdateProducer;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction createTransaction(Transaction transaction) {
        Transaction savedTrans = transactionRepository.save(transaction);

        // Send Kafka event - debit
        BalanceUpdateEvent debitEvent = new BalanceUpdateEvent(
            savedTrans.getSenderAccountNumber(),
            savedTrans.getAmount(),
            "DEBIT"
        );
        balanceUpdateProducer.sendBalanceUpdate(debitEvent);

        // Send Kafka event - credit
        BalanceUpdateEvent creditEvent = new BalanceUpdateEvent(
            savedTrans.getReceiverAccountNumber(),
            savedTrans.getAmount(),
            "CREDIT"
        );
        balanceUpdateProducer.sendBalanceUpdate(creditEvent);
        
        return savedTrans;
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
