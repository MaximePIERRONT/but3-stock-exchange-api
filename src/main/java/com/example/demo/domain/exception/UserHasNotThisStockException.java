package com.example.demo.domain.exception;

public class UserHasNotThisStockException extends Exception {
    public UserHasNotThisStockException() {
        super("User has not this stock");
    }
}
