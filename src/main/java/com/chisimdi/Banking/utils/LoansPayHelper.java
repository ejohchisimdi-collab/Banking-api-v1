package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class LoansPayHelper {
    @NotNull
            @Positive
    int userId;
    @NotNull
            @Positive
    int scheduleId;
    @NotNull
            @Positive
    double amount;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

}
