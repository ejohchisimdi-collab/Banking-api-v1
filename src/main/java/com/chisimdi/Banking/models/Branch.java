package com.chisimdi.Banking.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;


@Entity
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotNull
    String location;
    @OneToMany(mappedBy = "branch")
    List<User>employees;
    @OneToMany(mappedBy = "branch")
    List<User>customers;
@NotNull
        @Email
    String contactInfo;
@NotNull

@Pattern(regexp = "^(\\d{1,2})(am|pm)-(\\d{1,2})(am|pm)$",message = "must follow this format 1am-2pm")
    String hoursOfOperation;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setHoursOfOperation(String hoursOfOperation) {
        this.hoursOfOperation = hoursOfOperation;
    }

    public String getHoursOfOperation() {
        return hoursOfOperation;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public String getLocation() {
        return location;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<User> getCustomers() {
        return customers;
    }

    public void setCustomers(List<User> customers) {
        this.customers = customers;
    }

}
