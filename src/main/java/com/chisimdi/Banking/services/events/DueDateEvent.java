package com.chisimdi.Banking.services.events;

public class DueDateEvent {
    int loanId;
    String name;
    String contactInfo;
    String status;

    public DueDateEvent(String name, String contactInfo, String status,int loanId){
        this.name=name;
        this.contactInfo=contactInfo;
        this.status=status;
        this.loanId=loanId;
    }

    public String getStatus() {
        return status;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getName() {
        return name;
    }

    public int getLoanId() {
        return loanId;
    }
}
