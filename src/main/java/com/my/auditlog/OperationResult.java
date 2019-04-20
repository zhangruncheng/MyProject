package com.my.auditlog;

import lombok.Getter;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  15:22
 * @Version : 1.0.0
 **/
@Getter
public enum OperationResult {

    SUCCESS("SUCCESS","成功"),
    FAILED("FAILED","失败");

    private String resultCode;

    private String resultDesc;

    OperationResult(String resultCode,String resultDesc){
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
    }
}
