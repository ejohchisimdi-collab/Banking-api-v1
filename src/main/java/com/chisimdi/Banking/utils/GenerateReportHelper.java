package com.chisimdi.Banking.utils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class GenerateReportHelper {
    @NotNull
            @Valid
    int userId;
    @NotNull
            @Valid
    int accountId;
    @NotNull
            @Valid
    int month;
    @NotNull
            @Valid
    int year;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

}
