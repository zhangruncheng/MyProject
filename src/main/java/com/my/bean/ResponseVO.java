package com.my.bean;

import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  11:33
 * @Version : 1.0.0
 **/
@Data
public class ResponseVO extends LinkedHashMap<String,Object> {





    public ResponseVO(){
        this.setCode("200");
        this.setMessage("请求成功");
    }

    public ResponseVO(String code,String message){
        this.setCode(code);
        this.setMessage(message);
    }

    public ResponseVO setCode(String code){
        this.put("code",code);
        return this;
    }

    public String getCode(){
        return (String)this.get("code");
    }


    public ResponseVO setMessage(String message){
        this.put("message",message);
        return this;
    }

    public String getMessage(){
        return (String)this.get("message");
    }

    public ResponseVO setData(Object data){
        this.put("data",data);
        return this;
    }

    public Object getData(){
        return this.get("data");
    }
}
