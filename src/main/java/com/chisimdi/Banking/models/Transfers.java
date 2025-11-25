package com.chisimdi.Banking.models;

import jakarta.persistence.*;

import javax.naming.Name;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Transfers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @ManyToOne
            @JoinColumn(name = "transferring_account_id")
    Account transferringAccount;
    @ManyToOne
            @JoinColumn(name = "receiving_account_id")
    Account receivingAccount;

    double amount;
    String reversed="False";
    LocalDate localDate=LocalDate.now();

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getReceivingAccount() {
        return receivingAccount;
    }

    public Account getTransferringAccount() {
        return transferringAccount;
    }

    public void setReceivingAccount(Account receivingAccount) {
        this.receivingAccount = receivingAccount;
    }

    public void setTransferringAccount(Account transferringAccount) {
        this.transferringAccount = transferringAccount;
    }

    public String getReversed() {
        return reversed;
    }

    public void setReversed(String reversed) {
        this.reversed = reversed;
    }

}
