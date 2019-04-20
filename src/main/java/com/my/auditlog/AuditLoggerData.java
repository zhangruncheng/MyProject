package com.my.auditlog;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-19  17:17
 * @Version : 1.0.0
 **/
@Data
public class AuditLoggerData {

    /** 公司编码 */
    private @JsonProperty("company_code") String companyCode;
    
    /**  */
    private @JsonProperty("operation_object") String operationObject;

    /** 失败原因 */
    private @JsonProperty("failReason") String failReason;

    /**  */
    private @JsonProperty("operation_userType") String operationUserType;
    
}
