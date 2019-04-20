package com.my.auditlog;

import lombok.Getter;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  15:26
 * @Version : 1.0.0
 **/
@Getter
public enum OperationType {

    login("login","登陆"),
    logout("logout","登出"),
    add("add","新增"),
    update("update","修改"),
    delete("delete","删除"),
    upload("upload","上传");

    private String typeCode;
    private String tpyeDesc;

    OperationType(String typeCode,String tpyeDesc){
        this.typeCode = typeCode;
        this.tpyeDesc = tpyeDesc;
    }
}
