package vn.com.lol.common.image;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER})
@Constraint(validatedBy = {IsImageValidation.class})
public @interface IsImage {
    String message() default "Invalid image file";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
