package com.anand.BootFinance.service.impl;

import com.anand.BootFinance.dto.*;
import org.springframework.web.bind.annotation.RequestMapping;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
    BankResponse login(LoginDto loginDto);

}
