package com.zdoryk.data.exception;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String message) {
        super(message);
    }
}