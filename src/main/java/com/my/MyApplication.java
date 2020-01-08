package com.my;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@MapperScan("com.my.dao")
@Slf4j
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        log.info("start run success");
    }

    public String tt(){
        List<String> eccResult = new ArrayList<>();
        List<String> haveTags = new ArrayList<>();
        List<String> noTags = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        for (String s : eccResult) {
            JSONObject jsonObject = JSON.parseObject(s);
            String ecc_tags = (String) jsonObject.get("ecc_tags");
            if (null != ecc_tags ){
                haveTags.add(s);
            } else {
                noTags.add(s);
            }
        }
        resultList.addAll(haveTags);
        resultList.addAll(noTags);
        return JSON.toJSONString(resultList);
    }

}
