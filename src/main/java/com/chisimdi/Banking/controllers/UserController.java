package com.chisimdi.Banking.controllers;

import com.chisimdi.Banking.models.LoginResponse;
import com.chisimdi.Banking.models.User;
import com.chisimdi.Banking.models.UserDTO;
import com.chisimdi.Banking.services.UserService;
import com.chisimdi.Banking.utils.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @Operation(summary = "Register users")
    @PostMapping("/register")
    public UserDTO register(@Valid @RequestBody User user){
        log.info("Post /register");
        return userService.RegisterUsers(user);
    }

    @Operation(summary = "find all users", description = "Find all users, accessible only to admin")
@PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/")
    public List<UserDTO>findAllUsers(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10")int size)
    {log.info("Get /users");
        return userService.findAllUsers(pageNumber, size);
    }

    @Operation(summary = "find all customers", description = "Find all customers, accessible only to admin")
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/customers")
    public List<UserDTO>findAllCustomers(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10")  int size){
        log.info("Get /customers");
        return userService.findALlCustomers(pageNumber, size);
    }

    @Operation(summary = "find all employees", description = "Find all employees accessible only to admin")
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/employees")
    public List<UserDTO>findAllEmployees(@RequestParam(defaultValue = "0")int pageNumber,@RequestParam(defaultValue = "10")int size ) {
        log.info("Get /employees");
        return userService.findAllEmployees(pageNumber,size);
    }
    @Operation(summary = "log in",description = "User log in endpoint ")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return userService.logIn(loginRequest.getUserName(),loginRequest.getPassword());
    }
    @Operation(summary = "find specific users ",description = "Find specific users available to all users with varying limits")
    @PreAuthorize("hasRole('ROLE_Admin') or principal.userId == #userId")
    @GetMapping("/users/{userId}")
    public UserDTO findUsersBYId(@PathVariable("userId")@Param("userId") int userId){
        return userService.findUserBYId(userId);
    }
}
