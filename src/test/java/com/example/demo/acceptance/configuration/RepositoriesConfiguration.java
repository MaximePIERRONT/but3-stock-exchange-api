package com.example.demo.acceptance.configuration;

import com.example.demo.adapters.InMemoryStockRepository;
import com.example.demo.adapters.InMemoryStockReserveRepository;
import com.example.demo.adapters.InMemoryUserRepository;
import com.example.demo.domain.repositories.StockRepository;
import com.example.demo.domain.repositories.StockReserveRepository;
import com.example.demo.domain.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RepositoriesConfiguration {

    @Bean
    @Scope("cucumber-glue")
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    @Scope("cucumber-glue")
    public StockReserveRepository stockReserveRepository() {
        return new InMemoryStockReserveRepository();
    }

    @Bean
    @Scope("cucumber-glue")
    public StockRepository stockRepository() {
        return new InMemoryStockRepository();
    }
}
