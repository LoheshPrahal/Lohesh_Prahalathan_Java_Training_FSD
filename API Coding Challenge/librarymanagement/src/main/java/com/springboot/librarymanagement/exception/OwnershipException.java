package com.springboot.librarymanagement.exception;

public class OwnershipException extends RuntimeException{
    public OwnershipException(String message){
        super(message);
    }
}
