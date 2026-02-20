package com.martin.taskmanager.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>  errorHandler (MethodArgumentNotValidException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("VALIDATION_ERROR");

        List<String> messages = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()){
            messages.add(error.getDefaultMessage());
        }

        errorResponse.setMessages(messages);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> errorResponse (NoSuchElementException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("NOT_FOUND");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> errorResponse (IllegalArgumentException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("VALIDATION_ERROR");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }




}
