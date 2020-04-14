package com.ttn.bootcamp.project.ecommerce.validators;

import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    private static final String pattern = "^[A-Za-z0-9+_.-]+@(.+)$";

    public boolean validateEmail(String email ) {
        return email.matches(pattern);
    }
}
