package com.springboot.librarymanagement.exception;

public class InvalidCallException extends RuntimeException{
    public InvalidCallException(String message){
        super(message);
    }
}
