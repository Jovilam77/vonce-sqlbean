package cn.vonce.sql.spring.datasource;

import cn.vonce.sql.spring.annotation.DbSource;
import cn.vonce.sql.spring.annotation.DbSwitch;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.uitls.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Repository;

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
@Repository
public class DataSourceAspect {

    @Pointcut("target(cn.vonce.sql.service.SqlBeanService)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String xid = TransactionalContextHolder.getXid();
        if (clazz.isAnnotationPresent(DbSource.class) && StringUtil.isBlank(xid)) {
            String methodName = joinPoint.getSignature().getName();
            Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
            String dataSource = null;
            DbSource dbSource = clazz.getAnnotation(DbSource.class);
            try {
                Method method = clazz.getMethod(methodName, parameterTypes);
                DbSwitch dbSwitch = method.getAnnotation(DbSwitch.class);
                if (dbSwitch != null && dbSwitch.value() == DbRole.SLAVE && dbSource.slave().length > 0) {
                    if (dbSource.slave().length == 1) {
                        dataSource = dbSource.slave()[0];
                    } else {
                        dataSource = dbSource.slave()[new Random().nextInt(dbSource.slave().length)];
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

