package com.example.demo.domain;

public class StockReserve {
    private final Stock stock;
    private int quantity;

    public StockReserve(Stock stock, int quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return stock.hashCode();
    }

    public int quantity() {
        return quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.stock.setPrice(price);
    }
}
