package com.maverickbank.exception;

public class InvalidCallException extends RuntimeException {
    public InvalidCallException(String message) {
        super(message);
    }
}
