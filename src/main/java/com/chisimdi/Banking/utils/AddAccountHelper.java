package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class AddAccountHelper {
    @NotNull(message = "Account number cannot be empty")
    String accountNumber;
    @NotNull(message = "Balance cannot be empty")
            @Positive(message = "Balance must be positive")
    Double balance;
    @NotNull(message = "Type cannot be empty")
            @Pattern(regexp = "^Savings|Checking$",message = "TYpe must be either Savings or Checking")
    String type;
    @NotNull(message = "User Id cannot be empty")
            @Positive(message ="user Id must be positive" )
    int userId;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public int getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

}
