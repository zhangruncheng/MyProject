package com.my.bean;

import lombok.Data;

import java.util.List;

@Data
public class Grade {

    private List<Teacher> teacherList;

    private List<Student> studentList;

}
