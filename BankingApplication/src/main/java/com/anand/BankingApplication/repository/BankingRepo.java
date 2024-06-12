package com.anand.BankingApplication.repository;

import com.anand.BankingApplication.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingRepo extends JpaRepository<Account,Long> {
}
