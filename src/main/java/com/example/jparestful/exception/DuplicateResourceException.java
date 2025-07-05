package com.example.jparestful.exception;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String resourceName, String field, String value) {
        super("DUPLICATE_RESOURCE", String.format("Resource %s already exists with %s: %s", resourceName, field, value));
    }
}
