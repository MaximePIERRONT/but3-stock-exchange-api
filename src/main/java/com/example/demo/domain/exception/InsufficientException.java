package com.example.demo.domain.exception;

public class InsufficientException extends Exception {
    public InsufficientException(String insufficientOf) {
        super("Insufficient " + insufficientOf);
    }
}
