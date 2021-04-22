package com.trustrace.student.aop;

public class AuthorizationException extends Exception{
    public AuthorizationException(String message){
        super(message);
    }
}
