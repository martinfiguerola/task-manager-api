package com.martin.taskmanager.exception;

import com.martin.taskmanager.model.Status;

public class InvalidStatusTransitionException extends RuntimeException{

    public InvalidStatusTransitionException(Status status) {
        super("Cannot transition to " + status);
    }
}
