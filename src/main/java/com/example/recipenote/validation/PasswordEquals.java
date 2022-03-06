package com.example.recipenote.validation;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordEqualsValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface PasswordEquals {


    String message() default "{com.example.recipenote.validation.PasswordEquals.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface List {
        PasswordEquals[] value();
    }
}
