package com.anand.BootFinance.service.impl;

import com.anand.BootFinance.dto.TransactionDto;
import com.anand.BootFinance.entity.Transaction;
import com.anand.BootFinance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
          Transaction transaction = Transaction.builder()
                  .transactionType(transactionDto.getTransactionType())
                  .accountNumber(transactionDto.getAccountNumber())
                  .amount(transactionDto.getAmount())
                  .status("SUCCESS")
                  .build();
          transactionRepository.save(transaction);
        System.out.println("Transaction Saved Successfully");

    }
}
