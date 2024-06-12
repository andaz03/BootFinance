package com.anand.BankingApplication.Model;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "bank_details")
@Data
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNo;
    private String accountHolderName;
    private double balance;
}
