package com.chisimdi.Banking.models;

public class LoginResponse {
    String token;
    int userId;
    String role;
    String userName;

    public LoginResponse(String userName, String role, int userId,String token){
        this.role=role;
        this.userId=userId;
        this.userName=userName;
        this.token=token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
