package com.my.controller;

import com.my.thread.ThreadCallablePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
