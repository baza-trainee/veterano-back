package com.zdoryk.data.core;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SQLInjectionValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSQLInjection {
    String message() default "Input contains potential SQL injection.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
