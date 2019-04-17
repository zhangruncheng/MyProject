package com.my.bean;

import com.my.annotation.NotNull;
import lombok.Data;

@Data
public class Teacher {

    @NotNull(groups = "")
    private String subject;

    @NotNull(groups = "name")
    private String className;


}
