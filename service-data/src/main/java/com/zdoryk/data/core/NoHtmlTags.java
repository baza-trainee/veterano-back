package com.zdoryk.data.core;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NoHtmlTagsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoHtmlTags {
    String message() default "Input contains forbidden HTML tags.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
