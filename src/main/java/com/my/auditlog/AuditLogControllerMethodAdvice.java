package com.my.auditlog;

import org.aopalliance.aop.Advice;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 对标记了日志埋点的controller 方法进行切面处理
 * @Author : zhangruncheng
 * @Date : 2019-04-19  16:06
 * @Version : 1.0.0
 **/
@Component
public class AuditLogControllerMethodAdvice extends StaticMethodMatcherPointcutAdvisor
        implements InitializingBean, MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    /**
     * Callback after a given method successfully returned.
     *
     * @param returnValue the value returned by the method, if any
     * @param method      method being invoked
     * @param args        arguments to the method
     * @param target      target of the method invocation. May be {@code null}.
     * @throws Throwable if this object wishes to abort the call.
     *                   Any exception thrown will be returned to the caller if it's
     *                   allowed by the method signature. Otherwise the exception
     *                   will be wrapped as a runtime exception.
     */
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {

    }

    /**
     * Callback before a given method is invoked.
     *
     * @param method method being invoked
     * @param args   arguments to the method
     * @param target target of the method invocation. May be {@code null}.
     * @throws Throwable if this object wishes to abort the call.
     *                   Any exception thrown will be returned to the caller if it's
     *                   allowed by the method signature. Otherwise the exception
     *                   will be wrapped as a runtime exception.
     */
    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        HttpServletRequest request = getCurrentRequest();
        if (null == request) {
            return;
        }
        AuditLogRequired annotation = AnnotationUtils.getAnnotation(method, AuditLogRequired.class);
        if (null != annotation) {
            String funcName = annotation.funcName();
            OperationType operationType = annotation.oprType();
            if (null == funcName || 0 == funcName.length()) {
                throw new IllegalArgumentException("代码未按规范开发，请检查并确保AuditLogger注解中的funcname参数不为空");
            }
            if (null == operationType) {
                throw new IllegalArgumentException("代码未按规范开发，请检查并确保AuditLogger注解中的oprType参数不为空");
            }

            String requestUrl = request.getRequestURI();

        }
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * Perform static checking whether the given method matches.
     * <p>If this returns {@code false} or if the {@link #isRuntime()}
     * method returns {@code false}, no runtime check (i.e. no
     * {@link #matches(Method, Class, Object[])} call)
     * will be made.
     *
     * @param method      the candidate method
     * @param targetClass the target class
     * @return whether or not this method matches statically
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        AuditLogRequired annotation = AnnotationUtils.findAnnotation(method, AuditLogRequired.class);
        return null != annotation;
    }

    @Override
    public ClassFilter getClassFilter() {
        return new ClassFilter() {
            @Override
            public boolean matches(Class<?> clazz) {
                return AnnotationUtils.isAnnotationDeclaredLocally(Controller.class,clazz);
            }
        };
    }

    @Override
    public void setAdvice(Advice advice) {
        throw new UnsupportedOperationException("不允许修改或配置advice");
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    private HttpServletRequest getCurrentRequest(){
        HttpServletRequest request = null;
        RequestAttributes reqAttrs = RequestContextHolder.getRequestAttributes();
        if (null != reqAttrs && reqAttrs instanceof ServletRequestAttributes) {
             request = ((ServletRequestAttributes) reqAttrs).getRequest();
        }
        return request;
    }
}
