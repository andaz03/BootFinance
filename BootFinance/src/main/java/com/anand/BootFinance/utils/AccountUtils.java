package com.anand.BootFinance.utils;
import java.time.*;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE="001";
    public static final String ACCOUNT_EXISTS_MESSAGE="A user is already existed";
    public static final String ACCOUNT_CREATION_SUCCESS ="002";
    public static final String ACCOUNT_CREATION_MESSAGE="Your account is Created";
    public static final String ACCOUNT_NOT_EXIST_CODE="003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE="Account not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS="User Account found ";
    public static final String ACCOUNT_CREDITED_SUCCESS="005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE="User Accounte Credited Successfully";
    public static final String INSUFFICIENT_BALANCE_MESSAGE="Insufficient amount in your account !";
    public static final String INSUFFICIENT_BALANCE_CODE="006";
    public static final String AMOUNT_DEBITED_SUCCESS_CODE="007";
    public static final String AMOUNT_DEBITED_SUCCESS="Amount Debited Successfully";
    public static final String TRANSFER_SUCCESSFUL_CODE="008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE="Tranfer is Successful";


   public static String generateAccountNumber()
   {
       Year currentYear = Year.now();
       int min  = 100000;
       int max = 999999;
       int randomNum = (int) Math.floor(Math.random()*(max - min  +1)+ min);
       String  year = String.valueOf(currentYear);
       String randomNumber = String.valueOf(randomNum);
       StringBuilder accountNumber = new StringBuilder();
       return accountNumber.append(year).append(randomNumber).toString();
   }

}
