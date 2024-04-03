package com.nhnacademy.http.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String name) {
        super(String.format("Object not found : %s", name));
    }
}
