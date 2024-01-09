package com.fw.core.annotation.validator;

import com.fw.core.annotation.FieldMatch;
import org.apache.commons.beanutils.PropertyUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = PropertyUtils.getProperty(value, this.firstFieldName);
            final Object secondObj = PropertyUtils.getProperty(value, this.secondFieldName);
            return firstObj == null && secondObj == null
                    || firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ex) {
            return false;
        }

    }
}
