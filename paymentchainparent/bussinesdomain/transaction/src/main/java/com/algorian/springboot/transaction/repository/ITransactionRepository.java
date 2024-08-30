package com.algorian.springboot.transaction.repository;

import com.algorian.springboot.transaction.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByIbanAccount(String ibanAccount);

}
