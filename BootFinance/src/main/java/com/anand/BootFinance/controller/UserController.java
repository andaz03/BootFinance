package com.anand.BootFinance.controller;

import com.anand.BootFinance.dto.*;
import com.anand.BootFinance.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Mangagement APIs")
public class UserController {
    @Autowired
    UserService userService;
    @Operation(
            summary = "Create New User Account",
            description = "Creating a new User and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 Created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest)
    {
        return userService.createAccount(userRequest);
    }
    @Operation(
            summary = "Balance Enquiry",
            description = "Given an account Number ,checking the account Balance of the user"

    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 Success"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request)
    {
        return userService.balanceEnquiry(request);
    }
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request)
    {
        return userService.nameEnquiry(request);
    }
    @PostMapping("/credit")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest)
    {
        return userService.creditAccount(creditDebitRequest);
    }
    @PostMapping("/debit")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest)
    {
        return userService.debitAccount(creditDebitRequest);
    }
    @PostMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest request)
    {
        return userService.transfer(request);
    }
    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto)
    {
        return userService.login(loginDto);
    }
}
