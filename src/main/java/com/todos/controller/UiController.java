package com.todos.controller;

import com.todos.manipulator.TodoService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UiController {
    private final TodoService todoService;

    @Autowired
    public UiController(final TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping("/home")
    public String getHomePage(@RequestParam(name = "page", defaultValue = "1") final int pageNumber,
                              @RequestParam(name = "d") final String date,
                              final Model model) {
        JSONObject result = todoService.get(pageNumber);

        return "index";
    }
}
