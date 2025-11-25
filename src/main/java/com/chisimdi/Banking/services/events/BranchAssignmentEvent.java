package com.chisimdi.Banking.services.events;

public class BranchAssignmentEvent {

    int branchId;
    String contactInfo;
    String name;

    public BranchAssignmentEvent(int branchId, String contactInfo, String name){
        this.branchId=branchId;
        this.contactInfo=contactInfo;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }



    public int getBranchId() {
        return branchId;
    }
}
