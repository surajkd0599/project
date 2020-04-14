package com.ttn.bootcamp.project.ecommerce.validators;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
