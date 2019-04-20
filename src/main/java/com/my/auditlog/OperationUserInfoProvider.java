package com.my.auditlog;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供当前用户等登陆信息
 * @Author : zhangruncheng
 * @Date : 2019-04-19  16:00
 * @Version : 1.0.0
 **/
public interface OperationUserInfoProvider {

     /**
      * 获取用户登陆信息，用于web层 接口实现
      * @Author : zhangruncheng
      * @Date : 2019-04-19 16:06
      * @param request
      * @return com.my.auditlog.OperationUser
     **/
    OperationUser getOperationUser(HttpServletRequest request);

}
