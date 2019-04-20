package com.my.auditlog;

import java.lang.annotation.*;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  15:57
 * @Version : 1.0.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLogRequired {

    /** 表明控制器接口方法对应点中文名称 */
    String funcName();

    /** 操作类型 如 登陆，上传，下载等 */
    OperationType oprType();
}
