package com.stocks.api.controller;

import com.stocks.api.manipulator.StockService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UiController {
    private StockService stockService;

    @Autowired
    public UiController(final StockService stockService) {
        this.stockService = stockService;
    }

    @RequestMapping("/")
    public String getHomePage(@RequestParam(name = "page", defaultValue = "1") final int pageNumber, final Model model) {
        JSONObject result = stockService.get(pageNumber);

        model.addAttribute("stocks", result.get("items"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("maxPage", result.get("maxPage"));

        return "index";
    }
}
