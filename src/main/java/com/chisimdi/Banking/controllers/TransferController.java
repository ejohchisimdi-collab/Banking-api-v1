package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.TransfersDTO;
import com.chisimdi.Banking.services.TransferService;
import com.chisimdi.Banking.utils.ScheduledTransferHelper;
import com.chisimdi.Banking.utils.TransferHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfers")
public class TransferController {
    private static final Logger log = LoggerFactory.getLogger(TransferController.class);
    private TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService=transferService;
    }
@Operation(summary = "Make a transfer ",description = "Make a transfer, accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #transferHelper.userId")
    @PostMapping("/transfer")
    public TransfersDTO transfer(@Valid @RequestBody TransferHelper transferHelper){
        log.info("Post /transfer");
        return transferService.transfer(transferHelper.getUserId(),transferHelper.getSendersBankId(),transferHelper.getReceiversBankId(),transferHelper.getAmount());
    }

    @Operation(summary = "Reverse a transfer ",description = "Reverse a transfer, accessible only to admins and employees")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee')")
    @PostMapping("/reversal/{transactionId}")
    public TransfersDTO reversal(@PathVariable("transactionId")int transactionId){
        log.info("Post /reversal/{transactionId }, transactionId {}",transactionId);
        return transferService.reverseTransfer(transactionId);
    }

    @Operation(summary = "Make a scheduled transfer",description = "schedule a transfer, acessible only to customers")
    @PostMapping("/scheduled-transfer")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #transferHelper.userId")
    public TransfersDTO scheduledTransfer(@Valid@RequestBody ScheduledTransferHelper transferHelper){
        log.info("Post /scheduled-transfer");
        return transferService.createScheduledTransfer(transferHelper.getYear(),transferHelper.getMonth(),transferHelper.getDay(),transferHelper.getUserId(),transferHelper.getReceivingAccountId(),transferHelper.getSendingAccountId(),transferHelper.getAmount());
    }

    @Operation(summary = "Find all reverse transfers ",description = "Find all transfers that are reversed, accessible only to employees and admins")
    @PreAuthorize("hasRole('ROLE_Employee') or hasRole('ROLE_Admin') ")
    @GetMapping("/reversed")
    public List<TransfersDTO> findAllReversed(@RequestParam(defaultValue = "0")int pageNumber, @RequestParam(defaultValue = "10")int size){
        log.info("Get /reversed");
        return transferService.findAllReversed(pageNumber,size);
    }

    @Operation(summary = "Locates all transfers ",description ="locates all transfers , only accessible to admins and employees" )
    @PreAuthorize("hasRole('ROLE_Employee') or hasRole('ROLE_Admin')")
    @GetMapping("/")
    public List<TransfersDTO>findAllTransfers(@RequestParam (defaultValue = "0")int pageNumber ,@RequestParam(defaultValue = "10")int size)
    {
        log.info("Get /transfers");
        return transferService.findAllTransfers(pageNumber,size);
    }

    @Operation(summary = "Locate specific transfer",description = "Locates specific transfer by id and user Id, accessible to all users with varying limitations")
    @PreAuthorize("hasRole('ROLE_Employee') or hasRole('ROLE_Admin')  or principal.userId == #userId")
    @GetMapping("/transfers/{transferId}/users/{userId}")
    public TransfersDTO findTransferById(@PathVariable("transferId")int transferId,@PathVariable("userId")int userId){
        log.info("Get /transfers/{transferId}/users/{userId}, transfer Id {},user Id {}",transferId,userId);
        return transferService.findTransferById(transferId,userId);
    }

    @Operation(summary = "Locate transfers by users",description = "Locates transfers belonging to users of specific id, accessible to all users with varying limitations")
    @PreAuthorize("hasRole('ROLE_Employee') or hasRole('ROLE_Admin')  or principal.userId == #userId")
@GetMapping("/transfers/{userId}")
    public List<TransfersDTO>findTransactionsByUsers(@PathVariable("userId")int userId,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size){
        return transferService.findAllTransactionsByUsers(userId,pageNumber,size);
}
}
