package com.my.controller;

import com.my.bean.Person;
import com.my.bean.ResponseVO;
import com.my.bean.Student;
import com.my.bean.Teacher;
import com.my.bean.check.CheckStudent;
import com.my.exception.BizException;
import com.my.service.BaseService;
import com.my.thread.ThreadCallablePool;
import com.my.thread.ThreadTest;
import com.my.util.ValidateUtil;
import com.my.util.check.Privacy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public String post(@Validated Person grade, BindingResult bindingResult){

        Student student = new Student();
        Teacher teacher = new Teacher();
//        teacher.setSubject("语文");
        teacher.setClassName("一班");
        List list = baseService.get(teacher);
        threadTest.threadSynchronization();
        logger.info("post --> *******************");
        threadTest.asynchronousThread();
        return CheckStudent.check(student);

    }

    /**
     * json 字符校验
     * @Author : zhangruncheng
     * @Date : 2019-04-19 21:34
     * @param student
     * @return com.my.bean.ResponseVO
    **/
    @PostMapping(value = "/postJson")
    public ResponseVO validatedJson(@RequestBody Student student){

        ResponseVO vo = new ResponseVO();
        try {
            ValidateUtil.validate(student,  Privacy.class);
        } catch (BizException e ){
            vo.setMessage(e.getMessage());
            vo.setCode(e.getCode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return vo;
    }

}
