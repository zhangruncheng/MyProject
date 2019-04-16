package com.my.annotation;

import java.lang.annotation.*;

/**
 * 主动 校验方式
 * @@uthor : zhangruncheng
 * @Date : 2019-04-16  14:54
 * @Version : 1.0.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Validate {
    int min() default 1;

    int max() default 10;

    boolean isNotNull() default false;
}
