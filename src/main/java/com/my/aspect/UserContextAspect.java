package com.my.aspect;

import com.my.aspect.context.UserContext;
import com.my.aspect.model.DataPermissionCollection;
import com.my.aspect.model.UserDataPermission;
import com.my.bean.BaseModel;
import com.my.dao.BaseDao;
import com.my.exception.BizException;
import com.my.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : zhangruncheng
 * @Date : 2019-04-18  14:23
 * @Version : 1.0.0
 **/
@Component
@Aspect
public class UserContextAspect {

    private static final Logger logger = LoggerFactory.getLogger(UserContextAspect.class);

    @Autowired
    private BaseService baseService;

    private static final String idCardNum = "idCardNum";

    /**
     * 身份证号切点
     * 扫描 service 包，已经所有子包
     * @Author : zhangruncheng
     * @Date : 2019-04-18 14:26
     * @param
     * @return void
    **/
    @NotEmpty
    @Pointcut("execution(* com.my.service..*.*(..))")
    public void idCard(){

    }

    /**
     * 基于注解点数据权限
     * @Author : zhangruncheng
     * @Date : 2019-04-18 14:32
     * @param
     * @return void
    **/
    @Pointcut("@annotation(com.my.annotation.DataPermit)")
    public void dataPermissionPointcut(){

    }

    /**
     * 进入方法之前拦截
     * @Author : zhangruncheng
     * @Date : 2019-04-18 14:34
     * @param joinPoint
     * @return void
    **/
    @Before("dataPermissionPointcut()")
    public void dataPermitBefore(JoinPoint joinPoint){
        String idCard = null;
        String[] argNames = null;
        Object[] argValues = null;

        try {
            String classType = joinPoint.getTarget().getClass().getName();
            Class<?> clazz = Class.forName(classType);
            String clazzName = clazz.getName();
            String methodName = joinPoint.getSignature().getName();

            argNames = getFieldsName(this.getClass(), clazzName, methodName);
            argValues = joinPoint.getArgs();

        } catch (ClassNotFoundException e) {
            logger.error("dataPermitBefore -->ClassNotFoundException 用户上下文切面异常",e);
        } catch (NotFoundException e) {
            logger.error("dataPermitBefore -->NotFoundException 用户上下文切面异常",e);
        }

        if (null == argNames || argNames.length < 1 ) {
            throw new BizException("无权限访问");
        }
        for (int i = 0; i < argNames.length; i++) {
            if (argValues[i] != null && argValues[i] instanceof String && idCardNum.equalsIgnoreCase(argNames[i])) {
                idCard = (String) argValues[i];
            }
        }
        if (null == idCard){
            if (argValues[0] != null && argValues[0] instanceof BaseModel){
                BaseModel baseModel = (BaseModel) argValues[0];
                if (null != baseModel) {
                    idCard = baseModel.getIdCard();
                }
            }
        }
        if (StringUtils.isEmpty(idCard)) {
            throw new BizException("无权限访问");
        }

        /** 支持查找操作 */
        List list = baseService.getList();
        UserDataPermission userDataPermission = new UserDataPermission();
        userDataPermission.setIdCard(list.get(0).toString());
        /** 查询的处理的数据，用map 代替 */
        Map<String, DataPermissionCollection> map = new HashMap<>();
        userDataPermission.setDataPermissionCollectionMap(map);
        UserContext.setUserDataPermission(userDataPermission);

    }


    /**
     * 得到方法参数名称
     * @Author : zhangruncheng
     * @Date : 2019-04-16 20:02
     * @param cls
     * @param clazzName
     * @param mehtodName
     * @return java.lang.String[]
     **/
    private String [] getFieldsName(Class cls, String clazzName,String mehtodName) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(mehtodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        String [] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;

        for (int i = 0, length = paramNames.length; i < length; i++) {
            /** paramNames 即参数名 */
            paramNames[i] = attribute.variableName(i + pos);
        }
        return paramNames;
    }
}
