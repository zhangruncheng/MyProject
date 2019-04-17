package com.my.controller;

import com.my.bean.Grade;
import com.my.bean.Student;
import com.my.bean.Teacher;
import com.my.bean.check.CheckStudent;
import com.my.service.BaseService;
import com.my.thread.ThreadCallablePool;
import com.my.thread.ThreadTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private ThreadCallablePool threadCallablePool;


    @Autowired
    private BaseService baseService;

    @Resource
    private ThreadTest threadTest;

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
        threadTest.threadSynchronization();
        logger.info("post --> *******************");
        threadTest.asynchronousThread();
        return CheckStudent.check(student);

    }

}
