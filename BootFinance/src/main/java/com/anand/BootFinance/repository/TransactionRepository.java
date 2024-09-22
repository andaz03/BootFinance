package com.anand.BootFinance.repository;

import com.anand.BootFinance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
