package com.my.auditlog;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  17:05
 * @Version : 1.0.0
 **/
public abstract class AuditLoggerContext {
    private static final int FAIL_REASON_MAX_LENGTH = 200;

    /** 当前请求是否需要进行日志埋点处理 */
    private static final ThreadLocal<Boolean> NEED_AUDIT_LOGGING_FLAG = new ThreadLocal<>();

    /** 当前请求点操作用户信息是否已经填充完毕 */
    private static final ThreadLocal<Boolean> OPR_USER_FILLED_FLAG = new ThreadLocal<>();

    /** 日志埋点内容 */
    private static final ThreadLocal<AuditLoggerData> CTX = new ThreadLocal<>();


    private static void setOperationObject(String operationObject,boolean isSensitivityData){
        if (null != operationObject && operationObject.length() > 0 && isSensitivityData) {
            operationObject = String.valueOf(operationObject.hashCode());
        }
        getOrCreateAuditLoggerData().setOperationObject(operationObject);
    }



    /**
     * 设置失败原因
     *
     * @Author : zhangruncheng
     * @Date : 2019-04-19 17:44
     * @param failReason
     * @return void
    **/
    public static void setFailReason(String failReason) {
        if (null != failReason && failReason.length() > FAIL_REASON_MAX_LENGTH) {
            failReason = failReason.substring(0,FAIL_REASON_MAX_LENGTH);
        }
        getOrCreateAuditLoggerData().setFailReason(failReason);
    }


    protected static AuditLoggerData getOrCreateAuditLoggerData(){
        AuditLoggerData data = CTX.get();
        if (null == data) {
            data = new AuditLoggerData();
            CTX.set(data);
        }
        return data;
    }

    protected static void restContext(){
        NEED_AUDIT_LOGGING_FLAG.remove();
        OPR_USER_FILLED_FLAG.remove();
        CTX.remove();
    }

    protected static void setNeedAuditLogging(boolean needAuditLogging) {
        NEED_AUDIT_LOGGING_FLAG.set(Boolean.valueOf(needAuditLogging));
    }


    /**
     *
     * @Author : zhangruncheng
     * @Date : 2019-04-19 17:38
     * @param oprUserFilledFlag
     * @return void
    **/
    protected static void setisOprUserFilledFlag(boolean oprUserFilledFlag){
        OPR_USER_FILLED_FLAG.set(Boolean.valueOf(oprUserFilledFlag));
    }

    /**
     * 判断当前请求是否设计之日埋点处理
     * @Author : zhangruncheng
     * @Date : 2019-04-19 17:38
     * @param
     * @return boolean
    **/
    protected static boolean isNeedAuditLogging(){
        Boolean b = NEED_AUDIT_LOGGING_FLAG.get();
        return null != b && b.booleanValue();
    }

    /**
     * 判断当前请求点操作点用户信息是否已经填充完毕
     * @Author : zhangruncheng
     * @Date : 2019-04-19 17:37
     * @param
     * @return boolean
    **/
    protected static boolean isOprUserFilled(){
        Boolean b = OPR_USER_FILLED_FLAG.get();
        return null != b && b.booleanValue();
    }
}
