package com.martin.taskmanager.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid request payload"
        );

        problemDetail.setTitle("Validation Error");


        Map<String, String> invalidFields = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()){
            invalidFields.put(error.getField(), error.getDefaultMessage());
        }

        problemDetail.setProperty("invalidFields", invalidFields);

        return problemDetail;
    }

    @ExceptionHandler(InvalidTaskStatusException.class)
    public ProblemDetail handleInvalidStatus (InvalidTaskStatusException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        problemDetail.setTitle("Invalid Task Status");

        return problemDetail;
    }

    @ExceptionHandler({UserNotFoundException.class, TaskNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );
        problemDetail.setTitle("Resource Not Found");

        return problemDetail;
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            UserHasActiveTasksException.class,
            InvalidStatusTransitionException.class,
            ImmutableTaskException.class
    })
    public ProblemDetail handleConflict(RuntimeException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );
        problemDetail.setTitle("Business Rule Violation");

        return problemDetail;
    }

}

