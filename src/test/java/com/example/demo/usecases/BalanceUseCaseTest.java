package com.example.demo.usecases;

import com.example.demo.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BalanceUseCaseTest {

    public static User user;
    final BalanceUseCase balanceUsecase = new BalanceUseCase();

    @BeforeEach
    void setUp() {
        user = new User("john.doe@unknown.com", "password", "John", "Doe");
    }

    @Test
    void addBalanceOnExistingAccountShouldSucceed() {
        int amountToAdd = 100;
        balanceUsecase.add(user, amountToAdd);
        assertEquals(amountToAdd, user.getBalance());
    }

    @Test
    void addBalanceOnNonExistingAccountShouldFail() {
        User user = null;
        int amountToAdd = 100;
        assertThrows(IllegalArgumentException.class, () -> balanceUsecase.add(user, amountToAdd));
    }

    @Test
    void addBalanceWithNegativeAmountShouldFail() {
        int amountToAdd = -100;
        assertThrows(IllegalArgumentException.class, () -> balanceUsecase.add(user, amountToAdd));
    }

    @Test
    void withdrawBalanceOnExistingAccountShouldSucceed() {
        user.setBalance(100);
        balanceUsecase.withdraw(user, 100);
        assertEquals(0, user.getBalance());
    }

    @Test
    void withdrawBalanceOnNonExistingAccountShouldFail() {
        User user = null;
        int amountToWithdraw = 100;
        assertThrows(IllegalArgumentException.class, () -> balanceUsecase.withdraw(user, amountToWithdraw));
    }

    @Test
    void withdrawBalanceWithNegativeAmountShouldFail() {
        int amountToWithdraw = -100;
        assertThrows(IllegalArgumentException.class, () -> balanceUsecase.withdraw(user, amountToWithdraw));
    }

    @Test
    void withdrawBalanceWithInsufficientFundsShouldFail() {
        user.setBalance(100);
        int amountToWithdraw = 200;
        assertThrows(IllegalArgumentException.class, () -> balanceUsecase.withdraw(user, amountToWithdraw));
    }

}