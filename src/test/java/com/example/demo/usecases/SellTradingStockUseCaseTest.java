package com.example.demo.usecases;

import com.example.demo.adapters.InMemoryAuthenticationGateway;
import com.example.demo.adapters.InMemoryStockRepository;
import com.example.demo.adapters.InMemoryStockReserveRepository;
import com.example.demo.adapters.InMemoryUserRepository;
import com.example.demo.domain.Stock;
import com.example.demo.domain.User;
import com.example.demo.domain.exception.*;
import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;
import com.example.demo.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SellTradingStockUseCaseTest {

    private static final AuthenticationGateway AUTHENTICATION_GATEWAY = new InMemoryAuthenticationGateway();
    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    private static final StockReserveRepository STOCK_RESERVE_REPOSITORY = new InMemoryStockReserveRepository();
    private static final StockRepository STOCK_REPOSITORY = new InMemoryStockRepository();
    public static final TradingStockUseCase TRADING_STOCK_USE_CASE = new TradingStockUseCase(AUTHENTICATION_GATEWAY,
            STOCK_RESERVE_REPOSITORY,
            STOCK_REPOSITORY);
    public static final String STOCK_CODE = "stockCode";
    public static final Stock STOCK = new Stock(STOCK_CODE, 10);

    @BeforeEach
    public void setUp() {
        STOCK_RESERVE_REPOSITORY.clear();
        AUTHENTICATION_GATEWAY.clear();
        STOCK_REPOSITORY.clear();
    }

    @Test
    public void sellWithNegativeStockNumberShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> TRADING_STOCK_USE_CASE.sell(-1, STOCK_CODE));
    }

    @Test
    public void sellWithZeroStockNumberShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> TRADING_STOCK_USE_CASE.sell(0, STOCK_CODE));
    }

    @Test
    public void sellWithNotEnoughStocksInUserReserveShouldFail() {
        authenticateUser();
        registerStockWithCodeWith10Quantity();
        AUTHENTICATION_GATEWAY.currentUser().ifPresent(user -> {
            Stock code = new Stock(STOCK_CODE, 10);
            user.getOwnedStocks().put(code, 1);
        });
        assertThrows(InsufficientException.class, () -> TRADING_STOCK_USE_CASE.sell(2, STOCK_CODE));
    }

    @Test
    public void sellWithNoAuthenticatedUserShouldFail() {
        assertThrows(NotAuthenticatedException.class, () -> TRADING_STOCK_USE_CASE.sell(1, STOCK_CODE));
    }

    @Test
    public void sellWithNullStockCodeShouldFail() {
        authenticateUser();
        assertThrows(IllegalArgumentException.class, () -> TRADING_STOCK_USE_CASE.sell(1, null));
    }

    @Test
    public void sellWithEmptyStockCodeShouldFail() {
        authenticateUser();
        assertThrows(IllegalArgumentException.class, () -> TRADING_STOCK_USE_CASE.sell(1, ""));
    }

    @Test
    public void sellWithNonExistingStockCodeShouldFail() {
        authenticateUser();
        assertThrows(StockNotFound.class, () -> TRADING_STOCK_USE_CASE.sell(1, "nonExistingStockCode"));
    }

    @Test
    public void sellWithValidStockCodeShouldSucceed() throws StockNotFound, NotAuthenticatedException, UserHasNotThisStockException, InsufficientException {
        authenticateUser();
        registerStockWithCodeWith10Quantity();
        User user = AUTHENTICATION_GATEWAY.currentUser().orElseThrow();
        user.getOwnedStocks().put(STOCK, 10);
        assertEquals(10, user.getOwnedStocks().get(STOCK));
        TRADING_STOCK_USE_CASE.sell(1, STOCK_CODE);
        assertEquals(9, user.getOwnedStocks().get(STOCK));
    }

    private void registerStockWithCodeWith10Quantity() {
        STOCK_REPOSITORY.register(STOCK);
        STOCK_RESERVE_REPOSITORY.addStockReserve(STOCK, 10);
    }

    private static void authenticateUser() {
        User user = new User("user@email.com", "password", "user", "user");
        USER_REPOSITORY.register(user);
        AUTHENTICATION_GATEWAY.authenticate(user, "password");
    }

}