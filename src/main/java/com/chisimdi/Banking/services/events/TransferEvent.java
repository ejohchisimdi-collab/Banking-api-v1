package com.chisimdi.Banking.services.events;

import org.springframework.stereotype.Component;


public class TransferEvent {
    String contactInfo;
    String name;
    String receiversAccountNumber;
    String sendersAccountNUmber;
    Double amount;

    public TransferEvent(String contactInfo, String name, String receiversAccountNumber,String sendersAccountNUmber, Double amount ){
        this.amount=amount;
        this.name=name;
        this.contactInfo=contactInfo;
        this.receiversAccountNumber=receiversAccountNumber;
        this.sendersAccountNUmber=sendersAccountNUmber;
        this.amount=amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getName() {
        return name;
    }

    public String getReceiversAccountNumber() {
        return receiversAccountNumber;
    }

    public String getSendersAccountNUmber() {
        return sendersAccountNUmber;
    }

}
