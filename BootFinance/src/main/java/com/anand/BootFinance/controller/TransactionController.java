package com.anand.BootFinance.controller;

import com.anand.BootFinance.entity.Transaction;
import com.anand.BootFinance.service.impl.BankStatement;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
public class TransactionController {
    @Autowired
    private BankStatement bankStatement;
    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,@RequestParam String startDate,@RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber,startDate,endDate);
    }
}
