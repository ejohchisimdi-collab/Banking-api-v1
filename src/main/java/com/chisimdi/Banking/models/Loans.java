package com.chisimdi.Banking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Loans {
    @Id
            @GeneratedValue
    int id;
    String type;
    String status;
    int totalPayments;
    double amountWithInterest;
    boolean autoPay=false;

    @OneToMany(mappedBy = "loans")
    List<LoanSchedule>loanSchedules=new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    Account account;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }



    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAmountWithInterest(double amountWithInterest) {
        this.amountWithInterest = amountWithInterest;
    }

    public double getAmountWithInterest() {
        return amountWithInterest;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public boolean isAutopay() {
        return autoPay;
    }

    public void setAutopay(boolean autopay) {
        this.autoPay = autopay;
    }


    public List<LoanSchedule> getLoanSchedules() {
        return loanSchedules;
    }



    public void setLoanSchedules(List<LoanSchedule> loanSchedules) {
        this.loanSchedules = loanSchedules;
    }

}
