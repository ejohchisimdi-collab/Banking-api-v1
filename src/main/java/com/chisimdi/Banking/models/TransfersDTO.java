package com.chisimdi.Banking.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransfersDTO {
    int id;
    Double amount;
    int transferringAccountId;
    int receivingAccountId;
    String reversed="False";
    LocalDate localDate= LocalDate.now();

    public Double getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getReceivingAccountId() {
        return receivingAccountId;
    }

    public int getTransferringAccountId() {
        return transferringAccountId;
    }

    public void setReceivingAccountId(int receivingAccountId) {
        this.receivingAccountId = receivingAccountId;
    }

    public void setTransferringAccountId(int transferringAccountId) {
        this.transferringAccountId = transferringAccountId;
    }

    public String getReversed() {
        return reversed;
    }

    public void setReversed(String reversed) {
        this.reversed = reversed;
    }
}
