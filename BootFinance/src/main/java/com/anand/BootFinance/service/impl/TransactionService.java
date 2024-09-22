package com.anand.BootFinance.service.impl;

import com.anand.BootFinance.dto.TransactionDto;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
