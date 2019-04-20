package com.my.auditlog;

import lombok.Getter;

/**
 * 日志埋点中点 设计操作用户
 * @Author : zhangruncheng
 * @Date : 2019-04-19  15:52
 * @Version : 1.0.0
 **/
@Getter
public enum OperationUserType {

    IDCARD("IDCARD","身份证号码"),
    MOBILE("MOBILE","手机号码");

    private String typeCode;

    private String typeDesc;

    OperationUserType(String typeCode,String typeDesc){
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }
}
