package com.todos.controller;

import com.todos.exceptions.ResourceNotFoundException;
import com.todos.exceptions.StockAlreadyExistsException;
import com.todos.model.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Handles exceptions that may happen in the application and performs a nice wrapping over
 * the exception with a meaningful message.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleResourceNotFound(final ResourceNotFoundException ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Resource not found.");
        final ErrorResponse errorResponse = new ErrorResponse("404", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = StockAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExist(final StockAlreadyExistsException ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Resource already exists.");
        final ErrorResponse errorResponse = new ErrorResponse("409", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintVoilationError(final DataIntegrityViolationException ex) {
        final String message = isPrimaryKeyVoilation(ex) ? "Unique index or primary key violation" : ex.getMessage();
        final ErrorResponse errorResponse = new ErrorResponse("409", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericErrors(final Exception ex) {
        final String message = Optional.of(ex.getMessage()).orElse("Error occurred while serving the request.");
        final ErrorResponse errorResponse = new ErrorResponse("500", message);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean isPrimaryKeyVoilation(final DataIntegrityViolationException ex) {
        final ConstraintViolationException c = (ConstraintViolationException) ex.getCause();
        final SQLException sqlException = (SQLException) c.getCause();
        int errorCode = sqlException.getErrorCode();

        return errorCode == 23001;
    }
}