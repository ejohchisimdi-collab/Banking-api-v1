package com.chisimdi.Banking.services.events;

public class LoansCreationEvent {
    String contactInfo;
    Double amount;
    String type;
    String accountNumber;

    public LoansCreationEvent(String contactInfo,double amount, String type,String accountNumber){
        this.contactInfo=contactInfo;
        this.amount=amount;
        this.type=type;
        this.accountNumber=accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
