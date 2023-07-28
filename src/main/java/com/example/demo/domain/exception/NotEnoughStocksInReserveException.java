package com.example.demo.domain.exception;

public class NotEnoughStocksInReserveException extends Exception {
    public NotEnoughStocksInReserveException() {
        super("Not enough stocks in reserve");
    }
}
