package com.sebastian.learningspringboot.resource;

public class ErrorMessage {
    private final String errorMessage;

    public ErrorMessage(String message) {
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


}