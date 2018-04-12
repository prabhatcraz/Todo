package com.todos.controller;

public class TaskkAlreadyExistsException extends Throwable {
    public TaskkAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
