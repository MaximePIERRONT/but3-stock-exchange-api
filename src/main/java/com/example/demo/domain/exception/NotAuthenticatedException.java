package com.example.demo.domain.exception;

public class NotAuthenticatedException extends Exception {
    public NotAuthenticatedException() {
        super("User is not authenticated");
    }
}
