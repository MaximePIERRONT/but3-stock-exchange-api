package com.example.demo.adapters;

import com.example.demo.domain.Stock;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryStockRepository implements StockRepository {
    Set<Stock> stocks = new HashSet<>();
    @Override
    public Optional<Stock> getStock(String stockCode) {
        return stocks.stream()
                .filter(stock -> stock.getCode().equals(stockCode))
                .findFirst();
    }

    @Override
    public void register(Stock stock) {
        stocks.add(stock);
    }

    @Override
    public void clear() {
        stocks.clear();
    }
}
