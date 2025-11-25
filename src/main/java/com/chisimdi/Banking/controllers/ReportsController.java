package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.services.ReportsService;
import com.chisimdi.Banking.utils.GenerateReportHelper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    public ReportsService reportsService;

    public ReportsController(ReportsService reportsService){
        this.reportsService=reportsService;
    }
@Operation(summary = "View Reports ",description = "Generate summary based on a given time period, accessible  to all users with varying limitations")
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee') or principal.userId == #generateReportHelper.userId")
    @PostMapping("/")
    public ResponseEntity<InputStreamResource>generateResource(@RequestBody GenerateReportHelper generateReportHelper)throws Exception{
        return reportsService.generateAccountReport(generateReportHelper.getUserId(), generateReportHelper.getAccountId(), generateReportHelper.getMonth(), generateReportHelper.getYear());
    }

}
