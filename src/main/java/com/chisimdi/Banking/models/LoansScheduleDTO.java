package com.chisimdi.Banking.models;

import java.time.LocalDate;

public class LoansScheduleDTO {
    int id;
    String status;
    Double amount;
    Double amountPaid;
    LocalDate dueDate;
    boolean notified=false;

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void setStatus(String status) {
        this.status = status;
    }

}
