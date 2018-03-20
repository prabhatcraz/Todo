package com.stocks.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class StockList {
    private List<Stock> stockList;
}
