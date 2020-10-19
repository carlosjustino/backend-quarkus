package br.com.justino.projeto7.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = { IntegerValues.Validator.class })
public @interface IntegerValues {

    String message() default "Não é permitido este valor";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public int[] values() default {};

    public class Validator implements ConstraintValidator<IntegerValues, Integer> {

        private int[] values;

        @Override
        public void initialize(final IntegerValues integerValues) {
            this.values = integerValues.values();
        }

        @Override
        public boolean isValid(final Integer integer, final ConstraintValidatorContext constraintValidatorContext) {
            boolean valid = false;
            if (integer == null) {
                valid = true;
            } else {
                for (int n = 0; n < this.values.length; n++) {
                    if (this.values[n] == integer.intValue()) {
                        valid = true;
                    }
                }
            }
            return valid;
        }
    }

}