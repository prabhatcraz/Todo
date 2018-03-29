package com.todos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A model representing error response.
 */
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    @Getter @Setter
    private String errorCode;

    @Getter @Setter
    private String erroMessage;
}