package com.todos.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TodoList {
    private List<Todo> todoList;
}
