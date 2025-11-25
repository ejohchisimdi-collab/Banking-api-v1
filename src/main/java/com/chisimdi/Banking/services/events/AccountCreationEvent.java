package com.chisimdi.Banking.services.events;

import org.springframework.stereotype.Component;


public class AccountCreationEvent {
    private int userId;
    private String contactInfo;
    private String accountNumber;
    private String name;

    public AccountCreationEvent(int userId,
                                String contactInfo,
                                String accountNumber,
                                String name){
        this.accountNumber =accountNumber;
        this.name=name;
        this.contactInfo=contactInfo;
        this.userId=userId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getName() {
        return name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
