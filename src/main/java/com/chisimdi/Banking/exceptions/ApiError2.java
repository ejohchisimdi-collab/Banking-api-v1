package com.chisimdi.Banking.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;

public class ApiError2 {
    int status;
    String message;
    LocalDateTime localDateTime;
    HashMap<String,String>errors=new HashMap<>();

    public ApiError2(int status,String message){
        this.status=status;
        this.message=message;
        this.localDateTime=LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setErrors(HashMap<String, String> errors) {
        this.errors = errors;
    }

    public HashMap<String, String> getErrors() {
        return errors;
    }
}
