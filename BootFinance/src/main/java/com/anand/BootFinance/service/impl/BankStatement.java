package com.anand.BootFinance.service.impl;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.anand.BootFinance.dto.EmailDetails;
import com.anand.BootFinance.entity.Transaction;
import com.anand.BootFinance.entity.User;
import com.anand.BootFinance.repository.TransactionRepository;
import com.anand.BootFinance.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;
    private static final String FILE="C:\\PDF\\statement.pdf";
    public List<Transaction> generateStatement(String accountNumber,String startDate, String endDate) throws DocumentException, FileNotFoundException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList=transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(start)).filter(transaction -> transaction.getCreatedAt().isBefore(end)).toList();
        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() +" "+ user.getLastName();
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document;
        document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document,outputStream);
        document.open();
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("BootFinance"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.GRAY);
        bankName.setPadding(20f);
        bankInfoTable.addCell(bankName);
        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: "+ startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name :" + customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address : "+ user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.GRAY);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.GRAY);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.GRAY);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.GRAY);
        status.setBorder(0);
        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);
        transactionList.forEach(transaction ->
                {
                    transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                    transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
                    transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
                    transactionsTable.addCell(new Phrase(transaction.getStatus()));
                }
                );
        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);
        document.close();
        EmailDetails emailDetails = EmailDetails.builder()
        .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Your Requested account statement attached")
                .attachment(FILE)
        .build();
        emailService.sendEmailWithAttachment(emailDetails);
        return transactionList;
    }
}
