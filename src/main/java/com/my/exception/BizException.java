package com.my.exception;

import lombok.Data;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-18  14:53
 * @Version : 1.0.0
 **/
@Data
public class BizException extends RuntimeException {

    private String code;

    private String message;

    public BizException(){

    }

    public BizException(String message){
        super(message);
        this.message = message;
    }

    public BizException(String message,Throwable cause) {
        super(message,cause);
        this.message = message;
    }

    public BizException(Throwable cause){
        super(cause);
    }

    public BizException(String code,String message,Throwable cause) {
        super(message,cause);
        this.code = code;
        this.message = message;
    }

    public BizException(String code,String message) {
        super();
        this.code = code;
        this.message = message;
    }


}

