package vip.ylove.demo.client.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class IncludeValidatorForString implements ConstraintValidator<Include, String> {

    private Set<String> includeSet;

    @Override
    public void initialize(Include include) {
        this.includeSet = new HashSet<>(Arrays.asList(include.value()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(includeSet) || includeSet.size() == 0) {
            return false;
        }

        if (Objects.isNull(value)) {
            return false;
        }

        return this.includeSet.contains(value);
    }
}
