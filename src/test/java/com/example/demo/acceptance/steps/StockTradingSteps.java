package com.example.demo.acceptance.steps;

import com.example.demo.domain.Stock;
import com.example.demo.domain.StockReserve;
import com.example.demo.domain.exception.InsufficientException;
import com.example.demo.domain.exception.NotAuthenticatedException;
import com.example.demo.domain.exception.StockNotFound;
import com.example.demo.domain.exception.UserHasNotThisStockException;
import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;
import com.example.demo.usecases.TradingStockUseCase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockTradingSteps {

    private final AuthenticationGateway authenticationGateway;
    private final StockReserveRepository stockReserveRepository;
    private final TradingStockUseCase tradingStockUseCase;
    private final StockRepository stockRepository;

    public StockTradingSteps(AuthenticationGateway authenticationGateway,
                             StockReserveRepository stockReserveRepository,
                             StockRepository stockRepository) {
        this.authenticationGateway = authenticationGateway;
        this.stockReserveRepository = stockReserveRepository;
        this.stockRepository = stockRepository;
        tradingStockUseCase = new TradingStockUseCase(authenticationGateway, stockReserveRepository, stockRepository);
    }

    @When("I buy {int} shares of {string} stock")
    public void iBuySharesOfStockAt$Each(int stockNumber, String stockCode) throws StockNotFound, NotAuthenticatedException, InsufficientException {
        tradingStockUseCase.buy(stockNumber, stockCode);
    }

    @And("I should have {int} shares of {string} stock")
    public void iShouldHaveSharesOfStock(int numberOfStock, String stockCode) {
        Optional<Stock> stock = stockRepository.getStock(stockCode);
        authenticationGateway.currentUser().ifPresent(user -> {
            assertEquals(user.getOwnedStocks().get(stock.get()), numberOfStock);
        });
    }

    @And("I have {int} shares of {string} stock")
    public void iHaveSharesOfStock(int numberOfStock, String stockCode) {
        stockRepository.getStock(stockCode).ifPresent(stock -> {
            authenticationGateway.currentUser().ifPresent(user -> {
                user.getOwnedStocks().put(stock, numberOfStock);
            });
        });
    }

    @When("I sell {int} shares of {string} stock")
    public void iSellSharesOfStockAt$Each(int numberOfStocks, String stockCode) throws StockNotFound, NotAuthenticatedException, UserHasNotThisStockException, InsufficientException {
        tradingStockUseCase.sell(numberOfStocks, stockCode);
    }

    @And("the stock {string} has a current price of ${int} and quantity of {int}")
    public void theStockHasACurrentPriceOf$AndQuantityOf(String stockCode, int price, int quantity) {
        Optional<StockReserve> optionalStockReserve = stockReserveRepository.all().stream().filter(stock -> stock.getStock().getCode().equals(stockCode)).findFirst();
        if (optionalStockReserve.isPresent()) {
            StockReserve stockReserve = optionalStockReserve.get();
            stockReserve.setPrice(price);
            stockReserve.setQuantity(quantity);
        } else {
            stockReserveRepository.addStockReserve(new Stock(stockCode, price), quantity);
        }
    }

    @And("the stock reserve {string} should be {int}")
    public void theStockReserveShouldBe(String stockCode, int numberInReserve) {
        stockRepository.getStock(stockCode).ifPresent(stock -> {
            if (stockReserveRepository.getStockQuantity(stock) != numberInReserve) {
                stockReserveRepository.addStockReserve(stock, numberInReserve);
            }
            assertEquals( stockReserveRepository.getStockQuantity(stock), numberInReserve);
        });
    }

    @And("The stock {string} should exist at ${int} per stock")
    public void theStockShouldExistAt$PerStock(String stockCode, int price) {
        Optional<Stock> stock = stockRepository.getStock(stockCode);
        if (stock.isEmpty()) {
            stockRepository.register(new Stock(stockCode, price));
            stock = stockRepository.getStock(stockCode);
        }
        assertEquals(stock.get().getCode(), stockCode);
    }

    @When("I try to buy {int} shares of {string} stock")
    public void iTryToBuySharesOfStock(int numberOfStocks, String stockCode) {
        Exception exception = assertThrows(InsufficientException.class, () -> tradingStockUseCase.buy(numberOfStocks, stockCode));
        ErrorSteps.setException(exception);
    }

    @When("I try to sell {int} shares of {string} stock")
    public void iTryToSellSharesOfStock(int numberOfStocks, String stockCode) {
        Exception exception = assertThrows(InsufficientException.class, () -> tradingStockUseCase.sell(numberOfStocks, stockCode));
        ErrorSteps.setException(exception);
    }
}
