package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.Notifications;
import com.chisimdi.Banking.services.MailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/notifications")
@RestController
public class NotificationController {
    @Autowired
    private MailService mailService;

    @Operation(summary = "View all notifications",description = "View all notifications, accessible only to admins and employees")
@PreAuthorize("hasRole('ROLE_Admin') or hasRole('ROLE_Employee')")
    @GetMapping("/")
    public Page<Notifications> findAllNotifications(@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "0") int pageNumber){
        return mailService.findAllNotifications(pageNumber,size);
    }
}
