package com.chisimdi.Banking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "user name cannot be empty")
    @Size(min = 3,message = "user name must be a minimum of 3 characters")
    private String userName;
    @NotNull(message = "name cannot be empty")
    @Pattern(regexp = "[A-Za-z]+",message = "Name must be made up of letters only")
    private String name;
    @NotNull(message = "roles cannot be empty")
    @Pattern(regexp = "^Customer|Employee|Admin$",message = "roles must be either Customer,Employee or Admin")
    private String roles;
    @NotNull(message = "contact info cannot be empty ")
    @Email
    private String contactInfo;
    @NotNull(message = "Password cannot be empty")
    private String password;
    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Account> accounts;
@JsonIgnore
    @ManyToOne
    private Branch branch;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getRoles() {
        return roles;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
