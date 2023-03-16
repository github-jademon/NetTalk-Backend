package com.example.nettalk.exception;

public class PasswordWrongException extends RuntimeException {
    public PasswordWrongException(){
        super("Password is Wrong");
    }

}