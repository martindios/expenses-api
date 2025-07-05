package com.example.jparestful.exception;

public class InvalidDataException extends BaseException {
    public InvalidDataException(String message) {
        super("INVALID_DATA",  message);
    }
}
