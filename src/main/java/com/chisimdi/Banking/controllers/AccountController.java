package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.AccountDTO;
import com.chisimdi.Banking.services.AccountService;
import com.chisimdi.Banking.utils.AddAccountHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/accounts")
@RestController
public class AccountController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService=accountService;
    }

    @Operation(summary = "Create Accounts",description = "Creates accounts, accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #accountHelper.userId")
    @PostMapping("/creation")
    public AccountDTO addAccounts(@Valid @RequestBody AddAccountHelper accountHelper){
        log.info("Post /creation");
        return accountService.addAccount(accountHelper.getAccountNumber(), accountHelper.getBalance(), accountHelper.getType(), accountHelper.getUserId());
    }
    @Operation(summary = "Find all accounts", description = "Locate all accounts, accessible only to admins and employees")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee')")
    @GetMapping("/accounts")
    public List<AccountDTO> findAllAccounts(@RequestParam(defaultValue = "10") int size,@RequestParam(defaultValue = "0") int pageNumber) {
        log.info("Get /accounts");
        return accountService.findAllAccounts(pageNumber,size);

    }
    @Operation(summary = "find accounts by user Id and bank Id ",description = "Locates accounts by bank id and userId accessible to all users with varying limits")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/banks/{bankId}/users/{userId}")
    public AccountDTO findAccountsById(@PathVariable("bankId")int bankId,@PathVariable("userId")int userId){
        log.info("Get /banks/{bankId}/users/{userId},bank Id {}, user Id {}",bankId,userId);
        return accountService.findAccountById(bankId,userId);
    }
    @Operation(summary = "find accounts by user",description = "accessible to all users with varying limits")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/users/{userId}")
    public List<AccountDTO>findAccountsByUser(@PathVariable("userId")int userId,@RequestParam(defaultValue = "0") int pageNumber,@RequestParam(defaultValue = "10") int size){
        log.info("/users/{userId}, userId {}",userId);
        return accountService.findAccountsByUser(userId,pageNumber,size);
    }
}
