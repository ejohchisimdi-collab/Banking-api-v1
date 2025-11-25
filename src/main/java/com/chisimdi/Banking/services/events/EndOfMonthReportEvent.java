package com.chisimdi.Banking.services.events;

import java.io.File;

public class EndOfMonthReportEvent {

    String name;
    String contactInfo;
    File file;
    String accountNumber;

    public EndOfMonthReportEvent(String name,String contactInfo,File file,String accountNumber) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.file = file;
        this.accountNumber = accountNumber;



    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public File getFile() {
        return file;
    }


}
