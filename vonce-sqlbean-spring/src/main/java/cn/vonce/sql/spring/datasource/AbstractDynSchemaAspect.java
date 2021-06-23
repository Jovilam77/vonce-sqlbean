package cn.vonce.sql.spring.datasource;

import cn.vonce.sql.provider.DynSchemaContextHolder;
import cn.vonce.sql.spring.annotation.DbSource;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.enumerate.DbRole;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * 数据源切换切点
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/6/23 23:32
 */
@Aspect
public abstract class AbstractDynSchemaAspect {

    public abstract String getSchema();

    @Pointcut("target(cn.vonce.sql.service.SqlBeanService)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        if (clazz.isAnnotationPresent(DbSource.class)) {
            DynSchemaContextHolder.setSchema(getSchema());
        }
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        DynSchemaContextHolder.clearSchema();
    }
}

