package com.chisimdi.Banking.exceptions;

public class ExistsException extends RuntimeException{
    public ExistsException(String message){
        super(message);
    }
}
