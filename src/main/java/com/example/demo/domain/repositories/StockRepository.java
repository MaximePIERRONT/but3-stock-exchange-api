package com.example.demo.domain.repositories;

import com.example.demo.domain.Stock;

import java.util.Optional;

public interface StockRepository {
    Optional<Stock> getStock(String stockCode);

    void register(Stock stock);

    void clear();
}
