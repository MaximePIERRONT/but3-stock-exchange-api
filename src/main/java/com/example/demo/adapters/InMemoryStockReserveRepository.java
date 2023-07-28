package com.example.demo.adapters;

import com.example.demo.domain.Stock;
import com.example.demo.domain.StockReserve;
import com.example.demo.domain.repositories.StockReserveRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryStockReserveRepository implements StockReserveRepository {
    Set<StockReserve> stockReserves = new HashSet<>();

    @Override
    public Set<StockReserve> all() {
        return stockReserves;
    }
    @Override
    public void clear() {
        stockReserves.clear();
    }

    @Override
    public void addStockReserve(Stock stock, int quantity) {
        stockReserves.add(new StockReserve(stock, quantity));
    }

    @Override
    public int getStockQuantity(Stock stock) {
        return stockReserves.stream()
                .filter(stockReserve -> stockReserve.getStock().equals(stock))
                .findFirst()
                .map(StockReserve::quantity)
                .orElse(0);
    }

    @Override
    public boolean removeStocksFromReserve(Stock stock, int stockNumber) {
        Optional<StockReserve> optionalStockReserve = stockReserves.stream()
                .filter(stockReserve -> stockReserve.getStock().equals(stock))
                .findFirst();
        if (optionalStockReserve.isPresent()) {
            StockReserve stockReserve = optionalStockReserve.get();
            if (stockReserve.quantity() >= stockNumber) {
                stockReserve.setQuantity(stockReserve.quantity() - stockNumber);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addStocksToReserve(Stock stock, int numberOfStock) {
        Optional<StockReserve> optionalStockReserve = stockReserves.stream()
                .filter(stockReserve -> stockReserve.getStock().equals(stock))
                .findFirst();
        if (optionalStockReserve.isPresent()) {
            StockReserve stockReserve = optionalStockReserve.get();
            stockReserve.setQuantity(stockReserve.quantity() + numberOfStock);
        } else {
            stockReserves.add(new StockReserve(stock, numberOfStock));
        }
    }
}
