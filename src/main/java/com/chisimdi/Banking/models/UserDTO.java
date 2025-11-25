package com.chisimdi.Banking.models;

public class UserDTO {
    private int Id;
    private String name;
    private String role;
    private String contactInfo;
    private int branchId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
}
