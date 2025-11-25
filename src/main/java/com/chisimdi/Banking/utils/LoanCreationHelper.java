package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class LoanCreationHelper {
    @NotNull
            @Positive
    int userId;
    @NotNull
            @Positive
    int accountId;
    @NotNull
            @Positive
    Double amount;
    @NotNull
        @Pattern(regexp = "^Mortgage|Student|Personal|Auto",message = "Type must either be Mortgage, Student, Personal or Auto")
    String type;

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

}
