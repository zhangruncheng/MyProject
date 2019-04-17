package com.my.annotation;

import java.lang.annotation.*;

/**
 * @@uthor : zhangruncheng
 * @Date : 2019-04-16  19:19
 * @Version : 1.0.0
 **/

@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckNull {

    String group() default "";
}
