package com.zdoryk.data.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class SQLInjectionValidator implements ConstraintValidator<NoSQLInjection, String> {

    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(".*([';]+|(--)+).*");

    @Override
    public void initialize(NoSQLInjection constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !SQL_INJECTION_PATTERN.matcher(value).matches();
    }
}
