package com.stocks.api;

import com.stocks.api.dal.StockRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.IOException;

/**
 * Application Entry Point.
 */
@SpringBootApplication
@EnableJpaAuditing
public class StartUp {
    private final StockRepository stockRepository;

    @Autowired
    public StartUp(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(StartUp.class, args);
    }
}