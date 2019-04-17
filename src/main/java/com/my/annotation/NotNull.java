package com.my.annotation;

import java.lang.annotation.*;

/**
 * @@uthor : zhangruncheng
 * @Date : 2019-04-16  19:22
 * @Version : 1.0.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {

    String [] groups() ;
}
