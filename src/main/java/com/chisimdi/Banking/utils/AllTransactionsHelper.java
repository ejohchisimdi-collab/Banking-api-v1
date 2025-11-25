package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AllTransactionsHelper {
    @NotNull(message = "user Id cannot be empty")
            @Positive(message = "user Id must be positive")
    int userId;
    @Positive(message = "bank Id must be positive")
            @NotNull(message = "Bank Id cannot be empty")
    int bankId;
    @Positive(message = "amount must be positive")
            @NotNull(message = "amount cannot be empty")
    double amount;

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBankId() {
        return bankId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

}
