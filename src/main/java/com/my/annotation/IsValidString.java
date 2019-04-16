package com.my.annotation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;



/**
 *  功能未实现
 * @Author : zhangruncheng
 * @Date : 2019-04-16 14:42
 * @param null
 * @return
**/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IsValidString.ValidStringChecker.class)
@Deprecated
public @interface IsValidString {

    String message() default "The string is isBlank.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

    class ValidStringChecker implements ConstraintValidator<com.my.annotation.IsValidString,String>{



        @Override
        public void initialize(IsValidString constraintAnnotation) {

        }


        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isNotBlank(value)){
                return false;
            }
            return true;
        }
    }

}
