package com.stocks.api.controller;

import com.google.common.collect.Lists;
import com.stocks.api.manipulator.StockService;
import com.stocks.api.model.Stock;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * A {@link org.springframework.stereotype.Controller} that handles the basic requests of getting
 * stock information, creating new stocks and updating the existing ones.
 */
@RestController
@RequestMapping("/api")
public class StocksController {
    private static Logger logger = LoggerFactory.getLogger(StocksController.class);

    private final StockService stockService;

    @Autowired
    public StocksController(final StockService stockService) {
        this.stockService = stockService;
    }

    @RequestMapping(value = "/stocks/{stockId}", method = RequestMethod.GET)
    public Stock get(@PathVariable final Long stockId) {
        return stockService.get(stockId);
    }

    @RequestMapping(value = "/stocks/{stockId}", method = RequestMethod.POST, headers = "Accept=application/json")
    public void update(@PathVariable final Long stockId, @RequestBody Stock stock) {
        stockService.updatePrice(stockId, stock.getPrice(), stock.getVersion());
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.POST, headers = "Accept=application/json")
    public Stock create(@RequestBody Stock stock) {
        return stockService.create(stock);
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public JSONObject getAll(@RequestParam(name = "page", defaultValue = "1") final int pageNumber) {
        if (pageNumber < 1) throw new IllegalArgumentException("Page number can not be less than 1");
        return stockService.get(pageNumber);
    }

    /*************************************************
     *
     * Below API exist for demo/testing purpose.
     * It would be easy to create many stocks at once.
     *
     *************************************************/
    @RequestMapping(value = "/stocks/bulk", method = RequestMethod.POST, headers = "Accept=application/json")
    public void putInBulk(@RequestBody final Stock[] stockList) {
        stockService.bulkCreate(Lists.newArrayList(stockList));
    }
}