package com.anand.BankingApplication.service;

import com.anand.BankingApplication.Model.Account;
import com.anand.BankingApplication.repository.BankingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankingService {
    @Autowired
    BankingRepo bankingRepo;
    public Account createAccount(Account account)
    {
        return bankingRepo.save(account);
    }
    public List<Account> getAllDetails() {
        return bankingRepo.findAll();
    }
    public Optional<Account> getById(long accountNo) {
        return bankingRepo.findById(accountNo);
    }
    public Account withdraw(long accountNo, double balance) {
        try {

            Account list = bankingRepo.findById(accountNo).orElseThrow(()-> new RuntimeException("Account not found"));
            double oldMoney = list.getBalance();
            if (oldMoney >= balance) {
                list.setBalance(oldMoney - balance);
            } else {
                System.out.println("balance is not enough");
            }
            return bankingRepo.save(list);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing withdrawal");
        }
    }

    public Account deposit(long accountNo, double balance) {
        try {
            Account bankingModel = bankingRepo.findById(accountNo).get();
            double newBalance = balance + bankingModel.getBalance();
            bankingModel.setBalance(newBalance);
            return bankingRepo.save(bankingModel);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error processing Deposite");
        }
    }

    public Account transferMoney(long userAccount, long targetAcc, double balance) {
        Account bankingModel1 = bankingRepo.findById(userAccount).get();
        Account bankingModel2 = bankingRepo.findById(targetAcc).get();
        if (bankingModel1.getBalance()>=balance)
        {
            withdraw(bankingModel1.getAccountNo(),balance);
            deposit(bankingModel2.getAccountNo(),balance);
        }
        else {
            System.out.println("Not enough balance");
        }
        return bankingModel1;
    }
    public String deleteAccount(long accountNo) {
        Account bankingModel = bankingRepo.findById(accountNo).get();
        bankingRepo.delete(bankingModel);
        return "Delete account no : "+accountNo+" succesfully";
    }

}
