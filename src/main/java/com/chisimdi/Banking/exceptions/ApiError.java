package com.chisimdi.Banking.exceptions;

import java.time.LocalDateTime;

public class ApiError {
    int status;
    String message;
    LocalDateTime localDateTime;

    public ApiError(int status,String message){
        this.status=status;
        this.message=message;
        this.localDateTime=LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

}
