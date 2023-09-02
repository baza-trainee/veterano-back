package com.zdoryk.data.core;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class NoHtmlTagsValidator implements ConstraintValidator<NoHtmlTags, String> {

    @Override
    public void initialize(NoHtmlTags constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String cleanedValue = Jsoup.clean(value, Whitelist.none());
        return value.equals(cleanedValue);
    }
}
