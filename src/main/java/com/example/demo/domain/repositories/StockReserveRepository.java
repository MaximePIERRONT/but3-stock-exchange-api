package com.example.demo.domain.repositories;

import com.example.demo.domain.Stock;
import com.example.demo.domain.StockReserve;

import java.util.Optional;
import java.util.Set;

public interface StockReserveRepository {
    Set<StockReserve> all();

    void clear();

    void addStockReserve(Stock stock, int quantity);

    int getStockQuantity(Stock stock);

    boolean removeStocksFromReserve(Stock stock, int stockNumber);

    void addStocksToReserve(Stock stock, int numberOfStock);
}
