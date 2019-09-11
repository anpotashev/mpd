package ru.net.arh.mpd.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Map;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Валидация мапы: проверка наличия в ней ключей
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = MapValidator.class)
public @interface MapKeys {
    String[] keys();
    String message() default "Values for {fields} are not present";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
