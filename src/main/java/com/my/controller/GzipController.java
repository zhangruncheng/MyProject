package com.my.controller;

import com.alibaba.fastjson.JSONObject;
import com.my.bean.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;



/**
 * 大报文压缩输出
 * @Author : zhangruncheng
 * @Date : 2019-04-19  21:34
 * @Version : 1.0.0
 **/
@RestController
public class GzipController {

    private static final Logger logger = LoggerFactory.getLogger(GzipController.class);

    @GetMapping("/getZip")
    public void get(HttpServletResponse response){

        List<Student> list = creatValue();
        String str = JSONObject.toJSONString(list);
        String encoding = "UTF-8";
        byte[] comp = compress(str, encoding);
        String compressType = "gzip";
        response.setHeader(HttpHeaders.CONTENT_ENCODING, compressType);
        response.setContentType(encoding);
        response.setContentLength(comp.length);
        try {
            OutputStream out = response.getOutputStream();
            out.write(comp);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;

    }

    @GetMapping("/getJson")
    public String get(){

        List<Student> list = creatValue();
        String str = JSONObject.toJSONString(list);

        return str;

    }

    private byte[] compress(String str, String encoding) {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }


    private List<Student> creatValue() {
        List<Student> list = new ArrayList<>();
        final int size = 3000;
        for (int i = 0; i < size; i++) {
            Student stu = new Student();
            stu.setIdCard("10086");
            stu.setName("123");
            stu.setSex("男");
            list.add(stu);
        }
        return list;
    }



}
