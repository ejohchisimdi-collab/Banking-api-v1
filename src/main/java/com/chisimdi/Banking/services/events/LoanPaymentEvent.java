package com.chisimdi.Banking.services.events;

public class LoanPaymentEvent {
    String contactInfo;
    int loanId;
    String name;
    double amount;
    double remaining;

    public LoanPaymentEvent(String contactInfo, int loanId, String name,double amount,double remaining){
        this.contactInfo=contactInfo;
        this.loanId=loanId;
        this.name=name;
        this.amount=amount;
        this.remaining=remaining;
    }
    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public double getAmount() {
        return amount;
    }

    public double getRemaining() {
        return remaining;
    }
}
