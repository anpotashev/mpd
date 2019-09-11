package ru.net.arh.mpd.validation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapValidator implements ConstraintValidator<MapKeys, Map> {

    private String[] keys;

    @Override
    public void initialize(MapKeys annotation) {
        keys = annotation.keys();
    }

    @Override
    public boolean isValid(Map value, ConstraintValidatorContext context) {
        List<String> list = Arrays.stream(keys).filter(s -> !value.containsKey(s)).collect(Collectors.toList());
        if (list.isEmpty()) return true;
        ((ConstraintValidatorContextImpl) context).addMessageParameter("fields", list);
        return false;
    }
}
