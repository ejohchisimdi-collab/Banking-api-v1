package com.chisimdi.Banking.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String accountNumber;
    private double balance;
    private String type;
    @OneToMany(mappedBy ="account",cascade = CascadeType.PERSIST)
    private List<Transactions>transactions;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "account",cascade = CascadeType.PERSIST)
    private List<Loans> loans;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transactions> getTransactions() {
        return transactions;
    }

    public User getUser() {
        return user;
    }

    public void setTransactions(List<Transactions> transactions) {
        this.transactions = transactions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLoans(List<Loans> loans) {
        this.loans = loans;
    }

    public List<Loans> getLoans() {
        return loans;
    }
}
