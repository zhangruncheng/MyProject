package com.my.bean.check;

import com.my.annotation.Validate;
import com.my.bean.Student;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @@uthor : zhangruncheng
 * @Date : 2019-04-16  15:10
 * @Version : 1.0.0
 **/
public class CheckStudent {

    private static final Logger logger = LoggerFactory.getLogger(CheckStudent.class);

    public static String check(Student student) {
        if (null == student) {
            logger.info("check --> 校验对象为空");
            return "校验对象为空";
        }

        Field[] fields = Student.class.getDeclaredFields();
        for (Field field : fields) {
            /** 有注解则校验 */
            if (field.isAnnotationPresent(Validate.class)){
                Validate annotation = field.getAnnotation(Validate.class);
                if ("age".equals(field.getName())){
                    if (annotation.isNotNull()){
                        StringBuilder sb = new StringBuilder(field.getName());
                        sb.append("不可为空");
                        logger.info("check --> "+ sb.toString());
                        return sb.toString();
                    }

                    if (student.getAge() < annotation.min() || student.getAge() > annotation.max()){
                        StringBuilder sb = new StringBuilder(field.getName());
                        sb.append("不在范围内");
                        logger.info("check --> "+ sb.toString());
                        return sb.toString();
                    }
                } else if ("name".equals(field.getName())){
                    if (StringUtils.isBlank(student.getName())) {
                        if (annotation.isNotNull()) {
                            StringBuilder sb = new StringBuilder(field.getName());
                            sb.append("不可为空");
                            logger.info("check --> " + sb.toString());
                            return sb.toString();
                        }
                    }


                } else if ( "sex".equals(field.getName())) {
                    if ( StringUtils.isBlank(student.getSex())) {
                        if (annotation.isNotNull()) {
                            StringBuilder sb = new StringBuilder(field.getName());
                            sb.append("不可为空");
                            logger.info("check --> " + sb.toString());
                            return sb.toString();
                        }
                    }

                } else if ( "hight".equals(field.getName())) {
                    if (StringUtils.isBlank(student.getHight())) {
                        if (annotation.isNotNull()) {
                            StringBuilder sb = new StringBuilder(field.getName());
                            sb.append("不可为空");
                            logger.info("check --> " + sb.toString());
                            return sb.toString();
                        }
                    }

                }
            }
        }
        return "success";

    }
}
