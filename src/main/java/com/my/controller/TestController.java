package com.my.controller;

import com.my.bean.Grade;
import com.my.bean.Student;
import com.my.bean.check.CheckStudent;
import com.my.thread.ThreadCallablePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private ThreadCallablePool threadCallablePool;

    @GetMapping(value = "/get")
    public String get(){
        threadCallablePool.getT();
        return "hello word";
    }

    @PostMapping(value = "/post")
    public String post(@RequestBody Grade grade){

        Student student = grade.getStudentList().get(0);
        return CheckStudent.check(student);

    }
}
