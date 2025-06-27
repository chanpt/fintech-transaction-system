package com.example.accountservice.repository;

import com.example.accountservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;   //Provide set of CRUD oprations 
import org.springframework.stereotype.Repository;               

@Repository // Tells framework that this interface is a data access layer component
public interface AccountRepository extends JpaRepository<Account, Long> {}