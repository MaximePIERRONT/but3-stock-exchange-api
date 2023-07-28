package com.example.demo.usecases;

import com.example.demo.adapters.InMemoryAuthenticationGateway;
import com.example.demo.adapters.InMemoryStockRepository;
import com.example.demo.adapters.InMemoryStockReserveRepository;
import com.example.demo.adapters.InMemoryUserRepository;
import com.example.demo.domain.Stock;
import com.example.demo.domain.User;
import com.example.demo.domain.exception.InsufficientException;
import com.example.demo.domain.exception.NotAuthenticatedException;
import com.example.demo.domain.exception.StockNotFound;
import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;
import com.example.demo.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuyTradingStockUseCaseTest {

    private static final AuthenticationGateway AUTHENTICATION_GATEWAY = new InMemoryAuthenticationGateway();
    private static final StockReserveRepository STOCK_RESERVE_REPOSITORY = new InMemoryStockReserveRepository();

    private static final StockRepository STOCK_REPOSITORY = new InMemoryStockRepository();

    private static final UserRepository USER_REPOSITORY = new InMemoryUserRepository();
    public static final TradingStockUseCase tradingStockUseCase = new TradingStockUseCase(AUTHENTICATION_GATEWAY, STOCK_RESERVE_REPOSITORY, STOCK_REPOSITORY);
    public static final String STOCK = "STOCK";

    @BeforeEach
    public void setUp() {
        STOCK_RESERVE_REPOSITORY.clear();
        USER_REPOSITORY.clear();
        AUTHENTICATION_GATEWAY.clear();
    }

    @Test
    public void buyWithNegativeStockNumberShouldFail() {
        authenticateUser();
        assertThrows(IllegalArgumentException.class, () -> tradingStockUseCase.buy(-1, "stockCode"));
    }

    @Test
    public void buyWithZeroStockNumberShouldFail() {
        authenticateUser();
        assertThrows(IllegalArgumentException.class, () -> tradingStockUseCase.buy(0, "stockCode"));
    }

    @Test
    public void buyWithNoAuthenticatedUserShouldFail() {
        assertThrows(NotAuthenticatedException.class, () -> tradingStockUseCase.buy(1, "stockCode"));
    }

    @Test
    public void buyWithNullStockCodeShouldFail() {
        authenticateUser();
        assertThrows(StockNotFound.class, () -> tradingStockUseCase.buy(1, null));
    }

    @Test
    public void buyWithEmptyStockCodeShouldFail() {
        authenticateUser();
        assertThrows(StockNotFound.class, () -> tradingStockUseCase.buy(1, ""));
    }

    @Test
    public void buyWithNonExistingStockCodeShouldFail() {
        authenticateUser();
        assertThrows(StockNotFound.class, () -> tradingStockUseCase.buy(1, "nonExistingStockCode"));
    }

    @Test
    public void buyWithExistingStockCodeShouldSucceed() throws StockNotFound, NotAuthenticatedException, InsufficientException {
        authenticateUser();
        Stock stock = new Stock(STOCK, 20);
        STOCK_RESERVE_REPOSITORY.addStockReserve(stock, 10);
        AUTHENTICATION_GATEWAY.currentUser().ifPresent(user -> user.setBalance(200));
        tradingStockUseCase.buy(1, STOCK);
        assertEquals(9, STOCK_RESERVE_REPOSITORY.getStockQuantity(stock));
        User user = AUTHENTICATION_GATEWAY.currentUser().orElseThrow();
        assertEquals(1, user.getOwnedStocks().get(stock));
        assertEquals(180, user.getBalance());
    }

    @Test
    public void buyWithExistingStockCodeAndNotEnoughBalanceShouldFail() {
        authenticateUser();
        STOCK_REPOSITORY.register(new Stock(STOCK, 20));
        STOCK_REPOSITORY.getStock(STOCK).ifPresent(stock -> {
            STOCK_RESERVE_REPOSITORY.addStockReserve(stock, 10);
        });
        AUTHENTICATION_GATEWAY.currentUser().ifPresent(user -> user.setBalance(10));
        assertThrows(InsufficientException.class, () -> tradingStockUseCase.buy(1, STOCK));
    }

    @Test
    public void buyWithExistingStockCodeAndNotEnoughStocksShouldFail() {
        authenticateUser();
        Stock stock = new Stock(STOCK, 20);
        STOCK_RESERVE_REPOSITORY.addStockReserve(stock, 1);
        AUTHENTICATION_GATEWAY.currentUser().ifPresent(user -> user.setBalance(200));
        assertThrows(InsufficientException.class, () -> tradingStockUseCase.buy(2, STOCK));
    }

    @Test
    public void buyStocksYouPreviouslyHadShouldSucceed() throws StockNotFound, NotAuthenticatedException, InsufficientException {
        authenticateUser();
        Stock stock = new Stock(STOCK, 20);
        STOCK_RESERVE_REPOSITORY.addStockReserve(stock, 10);
        AUTHENTICATION_GATEWAY.currentUser().ifPresent(user -> user.setBalance(200));
        tradingStockUseCase.buy(1, STOCK);
        assertEquals(9, STOCK_RESERVE_REPOSITORY.getStockQuantity(stock));
        User user = AUTHENTICATION_GATEWAY.currentUser().orElseThrow();
        assertEquals(1,user.getOwnedStocks().get(stock));
        tradingStockUseCase.buy(1, STOCK);
        assertEquals(8, STOCK_RESERVE_REPOSITORY.getStockQuantity(stock));
        assertEquals(2,user.getOwnedStocks().get(stock));
        assertEquals(160, user.getBalance());
    }

    private static void authenticateUser() {
        User user = new User("user@email.com", "password", "user", "user");
        USER_REPOSITORY.register(user);
        AUTHENTICATION_GATEWAY.authenticate(user, "password");
    }
}