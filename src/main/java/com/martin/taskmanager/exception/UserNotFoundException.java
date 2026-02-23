package com.martin.taskmanager.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
