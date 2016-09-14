package epizza.order;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AllowedCharsValidator implements ConstraintValidator<AllowedChars, String> {
    @Override
    public void initialize(AllowedChars constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.contains("O");
    }
}
