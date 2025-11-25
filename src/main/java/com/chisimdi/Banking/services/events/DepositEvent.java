package com.chisimdi.Banking.services.events;

import org.springframework.stereotype.Component;


public class DepositEvent {
   private String name;
    private String accountNumber;
    private Double amount;
    private String contactInfo;
public DepositEvent(String name,String accountNumber,Double amount,String contactInfo){
    this.name=name;
    this.accountNumber=accountNumber;
    this.amount=amount;
    this.contactInfo=contactInfo;
}
    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
