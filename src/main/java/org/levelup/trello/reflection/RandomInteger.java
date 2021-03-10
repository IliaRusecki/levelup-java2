package org.levelup.trello.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target(ElementType.FIELD)
@Target(value = {
        ElementType.FIELD,
        // ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface RandomInteger {

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

}
