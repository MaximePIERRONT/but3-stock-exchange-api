package com.example.demo.domain.exception;

public class StockNotFound extends Exception {
    public StockNotFound() {
        super("Stock not found");
    }
}
