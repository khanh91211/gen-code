package com.fw.common.annotation.validator;

import com.fw.common.annotation.Equal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualQuery implements ConstraintValidator<Equal, Object> {
    String fieldName;

    @Override
    public void initialize(final Equal constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
