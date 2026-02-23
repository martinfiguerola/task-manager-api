package com.martin.taskmanager.exception;

public class InvalidTaskStatusException extends RuntimeException{

    public InvalidTaskStatusException(String status) {
        super("Invalid task status: " + status);
    }
}
