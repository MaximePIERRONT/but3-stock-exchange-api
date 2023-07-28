package com.example.demo.usecases;

import com.example.demo.domain.User;

public class BalanceUseCase {
    public void add(User user, long amount) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be positive");
        user.setBalance(user.getBalance() + amount);
    }

    public void withdraw(User user, long amount) {
        if (user == null)
            throw new IllegalArgumentException("User must not be null");
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be positive");
        if (user.getBalance() < amount)
            throw new IllegalArgumentException("Insufficient funds");
        user.setBalance(user.getBalance() - amount);
    }
}
