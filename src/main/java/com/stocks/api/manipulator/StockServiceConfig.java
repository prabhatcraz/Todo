package com.stocks.api.manipulator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Provides dependencies for {@link StockService}
 */
@Configuration
public class StockServiceConfig {
    private final static int PAGE_SIZE = 5;

    @Bean
    public Integer getPageSize() {
        return Integer.valueOf(PAGE_SIZE);
    }
}
