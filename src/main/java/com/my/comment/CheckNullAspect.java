package com.my.comment;

import com.my.annotation.CheckNull;
import com.my.annotation.NotNull;
import com.my.bean.Info;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

/**
 * 非空校验切面
 * @Author : zhangruncheng
 * @Date : 2019-04-16  19:24
 * @Version : 1.0.0
 **/
@Component
@Aspect
public class CheckNullAspect {

    private ThreadLocal<Info> local = new ThreadLocal<Info>(){
        @Override
        protected Info initialValue(){
            return new Info();
        }
    };

    /** 拦截带checkNull 带方法 */
    @Pointcut("@annotation(com.my.annotation.CheckNull)")
    private void annotationPointCut() {
    }

    /** 环绕切面 */
    @Around("annotationPointCut()")
    public Object process(ProceedingJoinPoint pjp) throws Throwable{

        // 1、获取目标方法
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        // 1.1、设置info的类名和方法名
        Info info = local.get();

        String className = targetMethod.getDeclaringClass().getName();
        String methodName = targetMethod.getName();
        info.setClassName(className);
        info.setMethodName(methodName);

        // 2、获取方法参数和参数值
        Parameter[] parameters = targetMethod.getParameters();
        Object[] args = pjp.getArgs();

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            /** 获取zhuj */
            CheckNull annotation = parameter.getAnnotation(CheckNull.class);
            /** 不存在注解 忽略*/
            if (Objects.isNull(annotation)) {
                continue;
            }
            /** 校验参数 */
            boolean verify = verifyParameter(annotation.group(), parameter.getName(), args[i]);
            if (!verify) {
                throw new NullPointerException(local.get().toString() + "为空！");
            }

        }




        /** *********************** */


//        String classType = pjp.getTarget().getClass().getName();
//        Class<?> clazz = Class.forName(classType);
//        String clazzName = clazz.getName();
//        String methodName = pjp.getSignature().getName();
//        String[] arsNames = getFieldsName(this.getClass(), clazzName, methodName);
//        Object[] argValues = pjp.getArgs();

        return null;
    }

    private boolean verifyParameter(String groupName, String paramName, Object paramValue) throws Exception {
        // 1、设置info的参数名
        Info info = local.get();
        info.setParamName(paramName);
        // 2、校验参数本身是否为null
        if (Objects.isNull(paramValue)) {
            return false;
        }
        // 3、如果参数注解的group属性为""，则无需校验参数属性
        if (Objects.equals(groupName, "")) {
            return true;
        }
        // 4、校验类的字段
        Class<?> clazz = paramValue.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            NotNull fieldAnnotation = field.getAnnotation(NotNull.class);
            // 3.1、没有注解或者注解不包含指定分组
            if (Objects.isNull(fieldAnnotation) || !Arrays.asList(fieldAnnotation.groups()).contains(groupName)) {
                // 不需要校验
                continue;
            }
            field.setAccessible(true);
            // 3.2、获取属性值
            Object value = field.get(paramValue);
            if (Objects.isNull(value)) {
                //获取属性名
                String name = field.getName();
                info.setFieldName(name);
                return false;
            }
        }
        // 5、校验通过
        return true;
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
