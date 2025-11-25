package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.Branch;
import com.chisimdi.Banking.models.BranchDTO;
import com.chisimdi.Banking.models.UserDTO;
import com.chisimdi.Banking.services.BranchService;
import com.chisimdi.Banking.utils.BranchHelper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/branches")
@RestController
public class BranchController {
    private static final Logger log = LoggerFactory.getLogger(BranchController.class);
    private BranchService branchService;

    public BranchController(BranchService branchService){
        this.branchService=branchService;
    }

    @Operation(summary = "Creates a branch ", description = "Creates a branch , accessible only to admins ")
@PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/creation")
    public BranchDTO createNewBranch(@Valid @RequestBody Branch branch){
        return branchService.createBranch(branch);
    }

    @Operation(summary = "Assign employees to branch",description = "Assigns an employee to a branch accessible only to admins")
    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("/employees")
    public UserDTO AssignEmployees(@Valid @RequestBody BranchHelper branchHelper){
        log.info("Post /employees");
        return branchService.assignEmployeesToBranch(branchHelper.getUserId(), branchHelper.getBranchId());
    }
    @Operation(summary = "Registers customers for a branch",description = "Registers a customer to a branch accessible only to customers")
    @PreAuthorize("hasRole('ROLE_Customer') and principal.userId == #branchHelper.userId")
    @PostMapping("/customers")
    public UserDTO registerCustomers(@Valid @RequestBody BranchHelper branchHelper){
        log.info("post /customers");
        return branchService.registerUSerToBranch(branchHelper.getUserId(), branchHelper.getBranchId());
    }

    @Operation(summary = "Locates all branches",description = "Lists all branches, accessible only to admins")
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/")
    public List<BranchDTO>findAllBranches(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int pageNumber){
        log.info("Get /branches");
        return branchService.viewAllBranches(size,pageNumber);
    }

}
