package com.chisimdi.Banking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ScheduledTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
    Account transferringAccount;
    @ManyToOne
    Account receivingAccount;

    LocalDate localDate;

    double amount;

    String completed="False";

    public int getId() {
        return id;
    }

    public void setTransferringAccount(Account transferringAccount) {
        this.transferringAccount = transferringAccount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getTransferringAccount() {
        return transferringAccount;
    }

    public void setReceivingAccount(Account receivingAccount) {
        this.receivingAccount = receivingAccount;
    }

    public Account getReceivingAccount() {
        return receivingAccount;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
