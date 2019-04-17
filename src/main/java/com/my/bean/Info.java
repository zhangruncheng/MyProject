package com.my.bean;

import lombok.Data;

import java.util.Objects;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-17  17:05
 * @Version : 1.0.0
 **/
@Data
public class Info {
    /** 类名 */
    private String className;

    /** 方法名 */
    private String methodName;

    /** 参数名 */
    private String paramName;

    /** 属性名 */
    private String fieldName;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (Objects.nonNull(className)) {
            builder.append("类").append(className);
        }
        if (Objects.nonNull(methodName)) {
            builder.append("的方法").append(methodName);
        }
        if (Objects.nonNull(paramName)) {
            builder.append("的参数").append(paramName);
        }
        if (Objects.nonNull(fieldName)) {
            builder.append("的属性").append(fieldName);
        }

        return builder.toString();
    }

}
