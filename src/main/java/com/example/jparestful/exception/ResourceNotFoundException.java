package com.example.jparestful.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String resourceName, String identifier) {
        super("RESOURCE_NOT_FOUND", String.format("Resource %s not found with identifier %s", resourceName, identifier));
    }

    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }
}
