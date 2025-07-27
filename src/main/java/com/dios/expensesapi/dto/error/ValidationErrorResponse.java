package com.dios.expensesapi.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {

    // ================================
    // FIELDS
    // ================================

    private String errorCode;
    private String message;
    private List<FieldError> fieldErrors;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    // ================================
    // CONSTRUCTORS
    // ================================

    public ValidationErrorResponse() {
        this.fieldErrors = new ArrayList<>();
    }

    public ValidationErrorResponse(String errorCode, String message, List<FieldError> fieldErrors, String path, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.fieldErrors = fieldErrors != null ? fieldErrors : new ArrayList<>();
        this.path = path;
        this.timestamp = timestamp;
    }

    // ================================
    // GETTERS AND SETTERS
    // ================================

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        this.fieldErrors = fieldErrors != null ? fieldErrors : new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    // ================================
    // BUILDER PATTERN
    // ================================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String errorCode;
        private String message;
        private List<FieldError> fieldErrors = new ArrayList<>();
        private String path;
        private LocalDateTime timestamp;

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors != null ? fieldErrors : new ArrayList<>();
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ValidationErrorResponse build() {
            return new ValidationErrorResponse(errorCode, message, fieldErrors, path, timestamp);
        }
    }

    // ================================
    // NESTED FIELD ERROR CLASS
    // ================================

    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;

        public FieldError() {
        }

        public FieldError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public static FieldErrorBuilder builder() {
            return new FieldErrorBuilder();
        }

        public static class FieldErrorBuilder {
            private String field;
            private Object rejectedValue;
            private String message;

            public FieldErrorBuilder field(String field) {
                this.field = field;
                return this;
            }

            public FieldErrorBuilder rejectedValue(Object rejectedValue) {
                this.rejectedValue = rejectedValue;
                return this;
            }

            public FieldErrorBuilder message(String message) {
                this.message = message;
                return this;
            }

            public FieldError build() {
                return new FieldError(field, rejectedValue, message);
            }
        }
    }

}
