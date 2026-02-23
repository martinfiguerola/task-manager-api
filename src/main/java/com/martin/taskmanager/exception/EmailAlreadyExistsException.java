package com.martin.taskmanager.exception;


public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String email) {
        super("The email " + email + " is already in use");
    }

}
