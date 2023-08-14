package com.zdoryk.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        // Check if the password contains at least one digit
        boolean containsDigit = password.matches(".*\\d.*");

        // Check if the password contains at least one special character
        boolean containsSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        // Check if the password contains at least one uppercase letter
        boolean containsUppercase = password.matches(".*[A-Z].*");

        // Combine all conditions
        return containsDigit && containsSpecialChar && containsUppercase;
    }
}
