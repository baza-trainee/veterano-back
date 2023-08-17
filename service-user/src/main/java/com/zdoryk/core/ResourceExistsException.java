package com.zdoryk.core;

public class ResourceExistsException extends RuntimeException{
    public ResourceExistsException(String message) {
        super(message);
    }
}