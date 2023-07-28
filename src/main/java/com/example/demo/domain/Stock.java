package com.example.demo.domain;

public class Stock {
    private final String code;
    private int price;

    public Stock(String code, int price) {
        this.code = code;
        this.price = price;
    }

    public int price() {
        return price;
    }

    public String getCode() {
        return code;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;

        if (price != stock.price) return false;
        return code.equals(stock.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}
