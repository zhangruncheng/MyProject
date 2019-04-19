package com.my.util;

import com.my.exception.BizException;
import org.springframework.validation.ValidationUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 基于 JSR303/JSR-349 和 spring 的post请求参数，
 * 手动校验
 * @Author : zhangruncheng
 * @Date : 2019-04-19  11:19
 * @Version : 1.0.0
 **/
public class ValidateUtil extends ValidationUtils {

    /**
     * 校验属性
     * @Author : zhangruncheng
     * @Date : 2019-04-19 14:23
     * @param object  校验类
     * @param groups  分组
     * @return void
    **/
    public static <T> void validate(final T object, final Class<?> ... groups){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<T>> result;
        result = validator.validate(object,groups.length == 0 ? new Class[0] : groups);

        if (!result.isEmpty()) {
            StringBuilder sb = new StringBuilder(64);
            for (ConstraintViolation<T> violation : result) {
                if (sb.length() > 0 ) {
                    sb.append(";");
                }
                sb.append(violation.getPropertyPath().toString());
                sb.append(":");
                sb.append(violation.getMessage());
            }
            throw new BizException("500", sb.toString());
        }
    }
}
