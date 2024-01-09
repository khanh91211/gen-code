package com.fw.common.annotation.validator;

import com.fw.common.annotation.ListMaxSize;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ListMaxSizeValidator implements ConstraintValidator<ListMaxSize, Object> {
    int maxSize;

    @Override
    public void initialize(final ListMaxSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            List<?> list = (List<?>) value;
            return list.size() <= maxSize;
        } catch (final Exception ex) {
            return false;
        }
    }
}
