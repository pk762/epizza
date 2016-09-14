package epizza.order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = { AllowedCharsValidator.class })
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedChars {

    String message() default "{epizza.order.AllowedChars.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
