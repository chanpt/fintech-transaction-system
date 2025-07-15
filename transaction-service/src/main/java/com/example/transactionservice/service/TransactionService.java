package com.example.transactionservice.service;

import com.example.transactionservice.kafka.event.BalanceUpdateEvent;
import com.example.transactionservice.kafka.producer.BalanceUpdateProducer;
import com.example.transactionservice.model.Transaction;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
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
        try {
            balanceUpdateProducer.sendBalanceUpdate(debitEvent);
        } catch (Exception exception) {
            log.error("Failed to send DEBIT event", exception);
}

        // Send Kafka event - credit
        BalanceUpdateEvent creditEvent = new BalanceUpdateEvent(
            savedTrans.getReceiverAccountNumber(),
            savedTrans.getAmount(),
            "CREDIT"
        );
        try {
            balanceUpdateProducer.sendBalanceUpdate(creditEvent);
        } catch (Exception exception) {
            log.error("Failed to send CREDIT event", exception);
        }
        
        return savedTrans;
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
