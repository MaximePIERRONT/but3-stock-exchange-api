package com.example.demo.usecases;

import com.example.demo.domain.Stock;
import com.example.demo.domain.User;
import com.example.demo.domain.exception.*;
import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;

import java.util.Optional;

public class TradingStockUseCase {

    private final AuthenticationGateway authenticationGateway;
    private final StockReserveRepository stockReserveRepository;
    private final StockRepository stockRepository;

    public TradingStockUseCase(AuthenticationGateway authenticationGateway,
                               StockReserveRepository stockReserveRepository,
                               StockRepository stockRepository) {
        this.authenticationGateway = authenticationGateway;
        this.stockReserveRepository = stockReserveRepository;
        this.stockRepository = stockRepository;
    }

    public void buy(int stockNumber, String stockCode) throws StockNotFound, NotAuthenticatedException, InsufficientException {
        throwExceptionIfInferiorAtZero(stockNumber);
        User user = authenticationGateway.currentUser().orElseThrow(NotAuthenticatedException::new);
        Stock stock = getStock(stockCode);
        checkIfUserHasEnoughFunds(stockNumber, user, stock);
        if (removeBoughtStocksFromStockReserve(stockNumber, stock)) {
            addBoughtStocksToUserStocks(stockNumber, user, stock);
            withdrawUserBalanceWithStocksTotalPrice(stockNumber, user, stock);
        } else
            throw new InsufficientException("stocks in reserve");
    }

    private boolean removeBoughtStocksFromStockReserve(int stockNumber, Stock stock) {
        return stockReserveRepository.removeStocksFromReserve(stock, stockNumber);
    }

    private static void checkIfUserHasEnoughFunds(long stockNumber, User user, Stock stock) throws InsufficientException {
        if (user.getBalance() < stockNumber * stock.price())
            throw new InsufficientException("funds");
    }

    private static void withdrawUserBalanceWithStocksTotalPrice(long stockNumber, User user, Stock stock) {
        user.setBalance(user.getBalance() - stockNumber * stock.price());
    }

    private static void addBoughtStocksToUserStocks(int stockNumber, User user, Stock stock) {
        if (user.getOwnedStocks().containsKey(stock))
            user.getOwnedStocks().put(stock, user.getOwnedStocks().get(stock) + stockNumber);
        else
            user.getOwnedStocks().put(stock, stockNumber);
    }

    private static void throwExceptionIfInferiorAtZero(int stockNumber) {
        if (stockNumber < 1)
            throw new IllegalArgumentException("Stock number must be superior to 0");
    }

    private Stock getStock(String stockCode) throws StockNotFound {
        Optional<Stock> optionalStock = stockRepository.getStock(stockCode);
        return optionalStock.orElseThrow(StockNotFound::new);
    }

    public void sell(int numberOfStock, String stockCode) throws InsufficientException, UserHasNotThisStockException, StockNotFound, NotAuthenticatedException {
        if (numberOfStock < 1)
            throw new IllegalArgumentException("Stock number must be superior to 0");
        if (stockCode == null || stockCode.isEmpty())
            throw new IllegalArgumentException("Stock code must not be empty");
        User user = authenticationGateway.currentUser().orElseThrow(NotAuthenticatedException::new);
        Stock stock = getStock(stockCode);
        if (user.getOwnedStocks().containsKey(stock)) {
            Integer countOfOwnedStock = user.getOwnedStocks().get(stock);
            if (countOfOwnedStock >= numberOfStock) {
                user.getOwnedStocks().put(stock, countOfOwnedStock - numberOfStock);
                stockReserveRepository.addStocksToReserve(stock, numberOfStock);
                user.setBalance(user.getBalance() + (long) numberOfStock * stock.price());
            } else
                throw new InsufficientException("stocks");
        } else
            throw new UserHasNotThisStockException();
    }
}
