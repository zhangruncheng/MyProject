package com.my.controller;

import com.my.bean.Grade;
import com.my.bean.Student;
import com.my.bean.Teacher;
import com.my.bean.check.CheckStudent;
import com.my.service.BaseService;
import com.my.thread.ThreadCallablePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private ThreadCallablePool threadCallablePool;


    @Autowired
    private BaseService baseService;

    @GetMapping(value = "/get")
    public String get(){
        threadCallablePool.getT();
        return "hello word";
    }

    @PostMapping(value = "/post")
    public String post(@RequestBody Grade grade){

        Student student = grade.getStudentList().get(0);
        Teacher teacher = new Teacher();
//        teacher.setSubject("语文");
        teacher.setClassName("一班");
        List list = baseService.get(teacher);
        return CheckStudent.check(student);

    }
}
