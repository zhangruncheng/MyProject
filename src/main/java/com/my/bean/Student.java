package com.my.bean;

import com.my.annotation.Validate;
import lombok.Data;

@Data
public class Student {

    @Validate(isNotNull = true)
    private String name;

    @Validate(isNotNull = true)
    private String sex;

    @Validate(min = 1,max = 100)

    private Integer age;

    @Validate(isNotNull = true)
    private String hight;
}
