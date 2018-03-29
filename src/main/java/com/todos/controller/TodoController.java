package com.todos.controller;

import com.todos.manipulator.TodoService;
import com.todos.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Controller for {@link Todo}.
 */
@RestController
@RequestMapping(value = "/api/todo", consumes = "application/json", produces = "application/json")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Todo get(@PathVariable final Long id) {
        return todoService.get(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Todo create(@RequestBody final Todo todo) {
        return todoService.create(todo);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Todo> getByDate(@RequestParam("date") @DateTimeFormat(pattern="MMddyyyy") Date date) {
        return todoService.getTodosByDate(date);
    }
}
