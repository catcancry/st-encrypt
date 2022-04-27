package vip.ylove.demo.client.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  use @Include(value = {"mm","nn"}, message="field1必须在('mm','nn')中")
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Include {

    String message() default "{value must be in range.}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 要包含的值
     */
    String[] value();
}
