package com.example.demo.domain;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final String mail;
    private String password;
    private String firstName;
    private String lastName;
    private long balance;

    private final Map<Stock,Integer> ownedStocks;

    public User(String mail, String password, String firstName, String lastName) {
        this.mail = mail;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = 0L;
        this.ownedStocks = new HashMap<>();
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public Map<Stock,Integer> getOwnedStocks() {
        return ownedStocks;
    }
}
