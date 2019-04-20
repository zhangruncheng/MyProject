package com.my.auditlog;

import lombok.Data;

/**
 * 当前登陆用户信息
 * @Author : zhangruncheng
 * @Date : 2019-04-19  16:01
 * @Version : 1.0.0
 **/
@Data
public class OperationUser {

    /** 当前用户操作类型 */
    private OperationUserType operationUserType;
    /** 当前用户操作id */
    private String operationUserId;
    /** 当前操作用户姓名 */
    private String operationUserName;


}
