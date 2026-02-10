package com.martin.taskmanager.exception;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse errorHandler (MethodArgumentNotValidException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(exception.getStatusCode().value());
        errorResponse.setError("VALIDATION_ERROR");

        List<String> messages = new ArrayList<>();

        for (FieldError error : exception.getBindingResult().getFieldErrors()){
            messages.add(error.getDefaultMessage());
        }

        errorResponse.setMessages(messages);

        return errorResponse;
    }


}
