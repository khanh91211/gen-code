package com.fw.common.annotation.validator;

import com.fw.common.annotation.DateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateFormatValidator implements ConstraintValidator<DateFormat, Object> {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public boolean isValid(Object date, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(date)) {
            return false;
        }
        if (date instanceof String) {
            if (date.toString() == null || date.toString().trim().isEmpty()) {
                return false;
            }
            try {
                Date ret = sdf.parse(date.toString().trim());
                if (sdf.format(ret).equals(date.toString().trim())) {
                    return true;
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }
}
