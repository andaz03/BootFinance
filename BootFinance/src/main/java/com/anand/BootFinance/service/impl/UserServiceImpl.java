package com.anand.BootFinance.service.impl;

import com.anand.BootFinance.config.JwtTokenProvider;
import com.anand.BootFinance.dto.*;
import com.anand.BootFinance.entity.Role;
import com.anand.BootFinance.entity.User;
import com.anand.BootFinance.repository.UserRepository;
import com.anand.BootFinance.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
       if(userRepository.existsByEmail(userRequest.getEmail()))
       {
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                   .accountInfo(null)
                   .build();
       }
       User newUser = User.builder()
               .firstName(userRequest.getFirstName())
               .lastName(userRequest.getLastName())
               .gender(userRequest.getGender())
               .address(userRequest.getAddress())
               .state(userRequest.getState())
               .accountNumber(AccountUtils.generateAccountNumber())
               .accountBalance(BigDecimal.ZERO)
               .email(userRequest.getEmail())
               .password(passwordEncoder.encode(userRequest.getPassword()))
               .phoneNumber(userRequest.getPhoneNumber())
               .status("ACTIVE")
               .role(Role.valueOf("ROLE_ADMIN"))
               .build();
        User savedUser = userRepository.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                        .recipient(savedUser.getEmail())
                       .subject("ACCOUNT CREATED")
                       .messageBody("Congratulations ! your account is created successfully\n Your account details are: "+savedUser.getFirstName() + " " + savedUser.getAccountNumber())
                               .build();


        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
       boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber()) ;
       if(!isAccountExist)
       {
           return BankResponse.builder()
                   .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                   .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                   .accountInfo(null)
                   .build();
       }
       User existedUser = userRepository.findByAccountNumber(request.getAccountNumber());
       return BankResponse.builder()
               .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
               .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
               .accountInfo(AccountInfo.builder()
                       .accountBalance(existedUser.getAccountBalance())
                       .accountNumber(existedUser.getAccountNumber())
                       .accountName(existedUser.getFirstName())
                       .build())
               .build();
    }
        public BankResponse  login(LoginDto loginDto)
        {
          Authentication authentication = null;
          authentication =  authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
          );
          EmailDetails loginAlert = EmailDetails.builder()
                  .subject("You're  logged in!")
                  .recipient(loginDto.getEmail())
                  .messageBody("You  logged into your account. If you did  not please contact bootfinance")
                  .build();
          emailService.sendEmailAlert(loginAlert);
          return BankResponse.builder()
                  .responseCode("Login Success")
                  .responseMessage(jwtTokenProvider.generateToken(authentication))
                  .build();

        }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist)
        {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());

            if(!isAccountExist)
            {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                        .accountInfo(null)
                        .build();
            }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
            userRepository.save(userToCredit);
            //save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                                    .accountName(userToCredit.getFirstName() + " "  + " " + userToCredit.getLastName())
                            .accountNumber(userToCredit.getAccountNumber())
                                            .accountBalance(userToCredit.getAccountBalance()).build())
                    .build();

    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isUserExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isUserExist) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }
   User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
      BigInteger availableBalance = (userToDebit.getAccountBalance().toBigInteger());
      BigInteger debitAmount = (request.getAmount().toBigInteger());
      if(availableBalance.intValue() < debitAmount.intValue())
      {
          return BankResponse.builder()
                  .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                  .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                  .accountInfo(null)
                  .build();
      }

      else{
          userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
          userRepository.save(userToDebit);
          TransactionDto transactionDto = TransactionDto.builder()
                  .accountNumber(userToDebit.getAccountNumber())
                  .transactionType("CREDIT")
                  .amount(request.getAmount())
                  .build();
          transactionService.saveTransaction(transactionDto);
          return BankResponse.builder()
                  .responseCode(AccountUtils.AMOUNT_DEBITED_SUCCESS_CODE)
                  .responseMessage(AccountUtils.AMOUNT_DEBITED_SUCCESS)
                  .accountInfo(AccountInfo.builder()
                          .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                          .accountNumber(request.getAccountNumber())
                          .accountBalance(userToDebit.getAccountBalance())
                          .build())
                  .build();
      }


    }

    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExist)
        {
            BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
       User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0)
        {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUserName = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();
        userRepository.save(sourceAccountUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("Your account is debited with " + " " + request.getAmount() + "\nAvailable Balance: "+sourceAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(debitAlert);
        User destinatedAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinatedAccountUser.setAccountBalance(destinatedAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinatedAccountUser);
        String recipientUserName = destinatedAccountUser.getFirstName() + " " + destinatedAccountUser.getLastName();
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("Credit ALERT")
                .recipient(destinatedAccountUser.getEmail())
                .messageBody("Your account is credited with " + " " + request.getAmount() + "\nAvailable Balance: "+destinatedAccountUser.getAccountBalance() + "\n The amount is transferred from " + sourceAccountUser.getAccountNumber() + " by " + sourceUserName)
                .build();
        emailService.sendEmailAlert(creditAlert);
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinatedAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
