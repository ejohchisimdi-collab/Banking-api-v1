package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.LoansDTO;
import com.chisimdi.Banking.models.LoansScheduleDTO;
import com.chisimdi.Banking.services.LoansService;
import com.chisimdi.Banking.utils.LoanCreationHelper;
import com.chisimdi.Banking.utils.LoansAutoPayHelper;
import com.chisimdi.Banking.utils.LoansPayHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoansController {
    private static final Logger log = LoggerFactory.getLogger(LoansController.class);
    private LoansService loansService;

    public LoansController(LoansService loansService){
        this.loansService=loansService;
    }

    @Operation(summary = "Takes a loan",description = "Takes a loan either personal, mortgage, student or auto. It is accessible only to customers  ")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #creationHelper.userId")
    @PostMapping("/creation")
    public LoansDTO takeALoan(@RequestBody @Valid LoanCreationHelper creationHelper){
        log.info("Post /creation");
        if(creationHelper.getType().equalsIgnoreCase("Mortgage")){
            return loansService.takeMortgageLoan(creationHelper.getUserId(),creationHelper.getAccountId(), creationHelper.getAmount());
        }
        if(creationHelper.getType().equalsIgnoreCase("Auto")){
            return loansService.takeAutoLoan(creationHelper.getUserId(),creationHelper.getAccountId(),creationHelper.getAmount());

        }
        if(creationHelper.getType().equalsIgnoreCase("Personal")){
            return loansService.takePersonalLoans(creationHelper.getUserId(),creationHelper.getAccountId(), creationHelper.getAmount());
        }
        if(creationHelper.getType().equalsIgnoreCase("Student")){
            return loansService.takeStudentLoans(creationHelper.getUserId(),creationHelper.getAccountId(), creationHelper.getAmount());
        }
        else {
            throw new IllegalArgumentException("Type must be either Personal,Auto,Student or Mortgage");
        }
    }

    @Operation(summary = "See all schedules for a particular loan",description = "looks for schedules for a particular loan, accessible  to all users ")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/schedules/loans/{loansId}/users/{userId}")
    public List<LoansScheduleDTO> seeAllSchedulesPerLoan(@PathVariable("loansId")int loansId, @PathVariable("userId")int userId){
        log.info("Get /schedules/{loansId}/users/{userId}");
        return loansService.findAllSchedulesPerLoan(loansId,userId);
    }

    @Operation(summary = "find a specific loan",description = "looks for a specific loan, accessible to all users with varying limits")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/users/{userId}/loans/{loansId}")
    public LoansDTO seeSpecificLoans(@PathVariable("userId")int userId,@PathVariable("loansId")int loansId){
        log.info("Post /users/{userId}/loans/{loansId}");
        return loansService.findSpecificLoans(loansId,userId);
    }

    @Operation(summary = "Locates all loans by user",description = "Locates all loans belonging to a specific user Id, accessible to all users with varying limits")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #userId")
    @GetMapping("/users/{userId}")
    public List<LoansDTO>findLoansByUsers(@PathVariable("userId")int userId,@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size){
        log.info("Get /users/{userId}");
        return loansService.findAllLoansByUser(userId,pageNumber,size);
    }

    @Operation(summary = "Finds all loans",description = "Locates all loans acessible only to")
    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') ")
    public List<LoansDTO>findAllLoans(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int pageNumber){
        return loansService.findAllLoans(size,pageNumber);
    }
    @Operation(summary = "Pay for loans",description = "Pays for loans, Only accessible to customers")
    @PostMapping("/pay")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #payhelper.userId")
    public LoansScheduleDTO pay(@RequestBody @Valid LoansPayHelper payHelper){
        log.info("Post /pay");
        return loansService.pay(payHelper.getUserId(), payHelper.getScheduleId(), payHelper.getAmount());
    }
    @Operation(summary = "Auto pay for loans",description = "Makes loan payment automatic, accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #helper.userId")
    @PostMapping("/auto-pay")
    public LoansDTO autoPay(@RequestBody @Valid LoansAutoPayHelper helper){
        log.info("post /auto-pay");
        return loansService.setAutoPay(helper.getUserId(),helper.getLoanId());
    }
}
