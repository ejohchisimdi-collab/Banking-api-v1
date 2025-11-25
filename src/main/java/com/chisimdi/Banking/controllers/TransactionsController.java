package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.TransactionsDTO;
import com.chisimdi.Banking.services.TransactionService;
import com.chisimdi.Banking.utils.AllTransactionsHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/transactions")
@RestController
public class TransactionsController {
    private static final Logger log = LoggerFactory.getLogger(TransactionsController.class);
    private TransactionService transactionService;

    public TransactionsController(TransactionService transactionService){
        this.transactionService=transactionService;
    }

    @Operation(summary = "Deposit",description = "Deposit money into an account, accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #allTransactionsHelper.userId")
    @PostMapping("/deposit")
    public TransactionsDTO deposit(@Valid @RequestBody AllTransactionsHelper allTransactionsHelper){
        log.info("Post /deposit");
        return transactionService.Deposit(allTransactionsHelper.getUserId(),allTransactionsHelper.getBankId(),allTransactionsHelper.getAmount());
    }

    @Operation(summary = "Withdrawal",description = "Withdraw money from an account, accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #transactionsHelper.userId")
    @PostMapping("/withdrawal")
    public TransactionsDTO withdrawal(@Valid@RequestBody AllTransactionsHelper transactionsHelper){
        log.info("Post /withdrawal");
        return transactionService.withdraw(transactionsHelper.getUserId(), transactionsHelper.getBankId(), transactionsHelper.getAmount());
    }

    @Operation(summary = "Get transactions by users",description = "Get transactions by customers, available to all Users, with varying limitations")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/users/{userId}")
    public List<TransactionsDTO>getTransactionsByUsers(@PathVariable("userId")int userId,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size){
        log.info("Get /users/{userId},userID {}",userId);
        return transactionService.findByUSers(userId,size,pageNumber);
    }

    @Operation(summary = "Get specific transaction",description = "Get a specific transaction by userId and transaction Id, accessible to all users with varying limitations")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/transactions/{transactionId}/users/{userId}")
    public TransactionsDTO getTransactionsById(@PathVariable("transactionId")int transactionId,@PathVariable("userId")int userId){
        log.info("Get /transactions/{transactionId}/users/{userId}, transaction ID {}, user Id {}",transactionId,userId);
        return transactionService.findTransactionsById(userId,transactionId);
    }
    @Operation(summary = "Get all transactions",description = "Get all transactions, accessible to admins and employees ")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee')")
    @GetMapping("/")
    public List<TransactionsDTO> getAllTransactions(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size){
        log.info("Get /transaction");
        return transactionService.findAllTransactions(pageNumber,size);
    }

}
