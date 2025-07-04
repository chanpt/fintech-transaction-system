package com.example.balanceservice.repository;

import com.example.balanceservice.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository; 
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByAccountNumber(String accountNumber);
}
