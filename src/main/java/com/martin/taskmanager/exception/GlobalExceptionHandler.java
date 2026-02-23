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

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("USER_NOT_FOUND");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("TASK_NOT_FOUND");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidTaskStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatus (InvalidTaskStatusException exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("INVALID_TASK_STATUS");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicate (EmailAlreadyExistsException exception) {
        ErrorResponse errorResponse  = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError("EMAIL_ALREADY_EXISTS");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

    }

    @ExceptionHandler(UserHasActiveTasksException.class)
    public ResponseEntity<ErrorResponse> handleUserHasTasks (UserHasActiveTasksException exception) {
        ErrorResponse errorResponse  = new ErrorResponse();

        errorResponse.setStatus(HttpStatus.CONFLICT.value());
        errorResponse.setError("USER_HAS_ACTIVE_TASKS");
        errorResponse.setMessages(List.of(exception.getMessage()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

    }
}
