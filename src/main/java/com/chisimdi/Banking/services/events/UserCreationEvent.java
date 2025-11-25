package com.chisimdi.Banking.services.events;

import org.springframework.stereotype.Component;
public class UserCreationEvent {
    private String contactInfo;
    private String name;

public UserCreationEvent(String contactInfo,String name){
    this.contactInfo=contactInfo;
    this.name=name;
}
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
