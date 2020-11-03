package cn.vonce.sql.spring.datasource;

import cn.vonce.sql.spring.annotation.DbSource;
import cn.vonce.sql.spring.annotation.DbSwitch;
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
 * @date 2020/10/29 21:24
 */
@Aspect
public class DataSourceAspect {

    @Pointcut("target(cn.vonce.sql.service.SqlBeanService)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        String dataSource = null;
        if (clazz.isAnnotationPresent(DbSource.class)) {
            DbSource dbSource = clazz.getAnnotation(DbSource.class);
            try {
                Method method = clazz.getMethod(methodName, parameterTypes);
                if (method.isAnnotationPresent(DbSwitch.class)) {
                    DbSwitch dbSwitch = method.getAnnotation(DbSwitch.class);
                    if (dbSwitch.value() == DbSwitch.Type.SLAVE) {
                        if (dbSource.slave().length > 1) {
                            dataSource = dbSource.slave()[new Random().nextInt(dbSource.slave().length + 1)];
                        } else {
                            dataSource = dbSource.slave()[0];
                        }
                    } else {
                        dataSource = dbSource.master();
                    }
                } else {
                    dataSource = dbSource.master();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            DataSourceContextHolder.setDataSource(dataSource);
        }
    }

    @After("pointcut()")
    public void after(JoinPoint joinPoint) {
        DataSourceContextHolder.clearDataSource();
    }
}

