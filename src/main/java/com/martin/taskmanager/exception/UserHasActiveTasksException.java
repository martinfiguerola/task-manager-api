package com.martin.taskmanager.exception;

public class UserHasActiveTasksException extends RuntimeException{

    public UserHasActiveTasksException(Long userId) {
        super("The user with id " + userId + " cannot be deleted because they have active tasks.");
    }
}
