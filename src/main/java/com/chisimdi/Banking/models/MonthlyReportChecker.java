package com.chisimdi.Banking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class MonthlyReportChecker {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    LocalDate localDate;
    @ManyToOne
    Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}
