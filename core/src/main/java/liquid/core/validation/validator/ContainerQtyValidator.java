package liquid.core.validation.validator;

import liquid.core.validation.constraints.ContainerQtyMax;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User: tao
 * Date: 10/15/13
 * Time: 7:25 PM
 */
public class ContainerQtyValidator implements ConstraintValidator<ContainerQtyMax, Integer> {
    private String fieldName;

    @Override
    public void initialize(ContainerQtyMax constraintAnnotation) {
        fieldName = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value < 5;
    }
}
