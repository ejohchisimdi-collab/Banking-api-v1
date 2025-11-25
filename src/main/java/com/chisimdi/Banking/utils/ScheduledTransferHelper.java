package com.chisimdi.Banking.utils;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ScheduledTransferHelper {
    @Positive
            @NotNull
    int year;
    @Positive
            @NotNull
    int month;
    @Positive
            @NotNull
    int day;
    @Positive
            @NotNull
    int userId;
    @Positive
            @NotNull
    int receivingAccountId;
    @Positive
            @NotNull
    int sendingAccountId;
    @Positive
            @NotNull
    double amount;

    public double getAmount() {
        return amount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setReceivingAccountId(int receivingAccountId) {
        this.receivingAccountId = receivingAccountId;
    }

    public int getReceivingAccountId() {
        return receivingAccountId;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getSendingAccountId() {
        return sendingAccountId;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setSendingAccountId(int sendingAccountId) {
        this.sendingAccountId = sendingAccountId;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
