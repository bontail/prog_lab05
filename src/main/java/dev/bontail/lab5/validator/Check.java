package main.java.dev.bontail.lab5.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Check {
    boolean notNull() default false;
    boolean notEmptyString() default false;
    long maxLong() default Long.MAX_VALUE;
    long minLong() default Long.MIN_VALUE;
    int maxInteger() default Integer.MAX_VALUE;
    int minInteger() default Integer.MIN_VALUE;
    short maxShort() default Short.MAX_VALUE;
    short minShort() default Short.MIN_VALUE;
    byte maxByte() default Byte.MAX_VALUE;
    byte minByte() default Byte.MIN_VALUE;
    double maxDouble() default Double.MAX_VALUE;
    double minDouble() default Double.MIN_VALUE;
    float maxFloat() default Float.MAX_VALUE;
    float minFloat() default Float.MIN_VALUE;
    boolean isEnum() default false;
}
