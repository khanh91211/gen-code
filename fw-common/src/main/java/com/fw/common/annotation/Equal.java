package com.fw.common.annotation;

import com.fw.common.annotation.validator.DateFormatValidator;
import com.fw.common.annotation.validator.EqualQuery;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = EqualQuery.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Equal {
    String message() default "Có lỗi khi truy vấn";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String fieldName();

    @Target({ METHOD, FIELD, ANNOTATION_TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Eq {
        Equal[] value();
    }
}
