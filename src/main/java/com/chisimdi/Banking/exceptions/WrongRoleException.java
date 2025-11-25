package com.chisimdi.Banking.exceptions;

public class WrongRoleException extends RuntimeException{
    public WrongRoleException(String message){
        super(message);
    }
}
