package com.my.auditlog;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  17:48
 * @Version : 1.0.0
 **/
public abstract class AuditLogCtxhelperForLogin {

    /**
     * 登陆用户类型
     * @Author : zhangruncheng
     * @Date : 2019-04-19 17:50
     * @param operationUserType
     * @return void
    **/
    public static void setOperationUserType(OperationUserType operationUserType) {
        if (null != operationUserType) {
            AuditLoggerContext.getOrCreateAuditLoggerData().setOperationUserType(operationUserType.getTypeCode());
        }
    }

}
