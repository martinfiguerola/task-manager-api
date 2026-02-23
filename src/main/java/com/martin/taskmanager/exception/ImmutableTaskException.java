package com.martin.taskmanager.exception;

import com.martin.taskmanager.model.Status;

public class ImmutableTaskException extends RuntimeException {
    public ImmutableTaskException(Status status) {
        super("Task is " + status + " and cannot be changed");
    }
}
