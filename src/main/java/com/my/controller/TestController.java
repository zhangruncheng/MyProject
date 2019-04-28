package com.my.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.my.bean.Person;
import com.my.bean.ResponseVO;
import com.my.bean.Student;
import com.my.bean.Teacher;
import com.my.bean.check.CheckStudent;
import com.my.common.GetClass;
import com.my.exception.BizException;
import com.my.jdk8.GroupBy;
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
import java.util.Map;

@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private ThreadCallablePool threadCallablePool;


    @Autowired
    private BaseService baseService;

    @Resource
    private ThreadTest threadTest;

    @Resource
    private GroupBy groupBy;

    @GetMapping(value = "/get")
    public String get(){
//        threadCallablePool.getT();
        groupBy.testSum();
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
    @PostMapping(value = "/postJson",produces = {"application/json;charset=UTF-8"})
    public String validatedJson(@RequestBody Student student){

        ResponseVO vo = new ResponseVO();
        StringBuilder sb = new StringBuilder();
        try {
            ValidateUtil.validate(student,  Privacy.class);
            String s = JSON.toJSONString(student, valueFilter);
            vo.setData("str");
            String s1 = JSON.toJSONString(vo);
            String replace = s1.replace("\"str\"", s);
            sb.append(replace);

        } catch (BizException e ){
            vo.setMessage(e.getMessage());
            vo.setCode(e.getCode());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    ValueFilter valueFilter = new ValueFilter() {
        Map<String,String> filedMap = null;
        @Override
        public Object process(Object object, String name, Object value) {
            if (null == filedMap) {
                filedMap = GetClass.getFiledMap(object.getClass());
            }
            if ("java.lang.String".equalsIgnoreCase(filedMap.get(name)) && null == value) {
                return "-";
            }

            return value;
        }
    };

}
