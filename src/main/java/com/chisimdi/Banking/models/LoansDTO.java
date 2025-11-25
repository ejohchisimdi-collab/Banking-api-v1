package com.chisimdi.Banking.models;

public class LoansDTO {
    int id;
    String status;
    Double amountWithInterest;
    String type;
    Boolean autoPay;


    public void setAmountWithInterest(Double amountWithInterest) {
        this.amountWithInterest = amountWithInterest;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getAmountWithInterest() {
        return amountWithInterest;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(Boolean autoPay) {
        this.autoPay = autoPay;
    }

}
