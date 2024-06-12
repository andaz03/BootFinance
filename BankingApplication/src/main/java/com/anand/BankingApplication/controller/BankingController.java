package com.anand.BankingApplication.controller;

import com.anand.BankingApplication.Model.Account;
import com.anand.BankingApplication.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("BootFinance")
public class BankingController {
    @Autowired
    BankingService bankingService;

    @PostMapping("/create")
    public ResponseEntity<Account> openAccount(@RequestBody Account account) {
        Account createdAccount = bankingService.createAccount(account);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/getAllDetails")
    public ResponseEntity<List<Account>> getAllDetails() {
        List<Account> allDetails = bankingService.getAllDetails();
        return new ResponseEntity<>(allDetails, HttpStatus.OK);
    }

    @GetMapping("/getById/{accountNo}")
    public ResponseEntity<Optional<Account>> getById(@PathVariable long accountNo) {
        Optional<Account> account = bankingService.getById(accountNo);
        if (account.isPresent()) {
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Account> withdraw(@RequestParam long accountNo, @RequestParam double balance) {
        try {

            Account updatedAccount = bankingService.withdraw(accountNo, balance);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestParam long accountNo, @RequestParam double balance) {
        try {
            Account updatedAccount = bankingService.deposit(accountNo, balance);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/transfer")
    public ResponseEntity<Account> transferMoney(@RequestParam long accountNo1, @RequestParam long accountNo2, @RequestParam double balance) {
        try {
            Account updatedAccount = bankingService.transferMoney(accountNo1, accountNo2, balance);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestParam long accountNo) {
        try {
            String result = bankingService.deleteAccount(accountNo);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
