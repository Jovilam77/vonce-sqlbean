package cn.vonce.sql.spring.datasource;

import cn.vonce.sql.spring.annotation.DbSource;
import cn.vonce.sql.spring.annotation.DbTransactional;
import cn.vonce.sql.spring.enumerate.DbRole;
import cn.vonce.sql.uitls.IdBuilder;
import cn.vonce.sql.uitls.StringUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Random;

/**
 * 事务拦截器
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/14 11:34
 */
public class TransactionalInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        DbTransactional dbTransactional = methodInvocation.getThis().getClass().getAnnotation(DbTransactional.class);
        if (methodInvocation.getMethod().isAnnotationPresent(DbTransactional.class)) {
            dbTransactional = methodInvocation.getMethod().getAnnotation(DbTransactional.class);
        }
        Object result;
        DbSource dbSource = null;
        try {
            String xid = TransactionalContextHolder.getXid();
            //已经存在事务则加入事务并执行
            if (StringUtil.isNotBlank(xid)) {
                result = methodInvocation.proceed();
                return result;
            }
            //当前没有事务则创建事务
            else {
                //多数据源注解
                dbSource = methodInvocation.getMethod().getDeclaringClass().getAnnotation(DbSource.class);
                //配置了多数据源
                if (dbSource != null) {
                    DbRole dbRole = dbTransactional.role();
                    if (dbRole != null && dbRole == DbRole.SLAVE && dbSource.slave().length > 0) {
                        if (dbSource.slave().length == 1) {
                            DataSourceContextHolder.setDataSource(dbSource.slave()[0]);
                        } else {
                            DataSourceContextHolder.setDataSource(dbSource.slave()[new Random().nextInt(dbSource.slave().length)]);
                        }
                    } else {
                        DataSourceContextHolder.setDataSource(dbSource.master());
                    }
                }
                TransactionalContextHolder.setXid(IdBuilder.uuid());
                result = methodInvocation.proceed();
                //移除事务id
                TransactionalContextHolder.clearXid();
                //提交或回滚事务
                ConnectionContextHolder.commit(true);
            }
        } catch (Throwable e) {
            //移除事务id
            TransactionalContextHolder.clearXid();
            Class<? extends Throwable>[] rollbackFor = dbTransactional.rollbackFor();
            Class<? extends Throwable>[] noRollbackFor = dbTransactional.noRollbackFor();
            if (rollbackFor.length > 0) {
                //遇到哪些异常回滚
                for (Class<? extends Throwable> thr : rollbackFor) {
                    if (thr.getClass().isAssignableFrom(e.getClass())) {
                        ConnectionContextHolder.commit(false);
                        break;
                    }
                }
            }
            if (noRollbackFor.length > 0) {
                //遇到哪些异常不回滚
                for (Class<? extends Throwable> thr : noRollbackFor) {
                    if (thr.getClass().isAssignableFrom(e.getClass())) {
                        ConnectionContextHolder.commit(true);
                        break;
                    }
                }
            }
            if (rollbackFor.length == 0 && noRollbackFor.length == 0) {
                //回滚事务
                ConnectionContextHolder.commit(false);
            }
            throw e;
        } finally {
            //配置了多数据源 则移除当前线程设定的数据源
            if (dbSource != null) {
                DataSourceContextHolder.clearDataSource();
            }
        }
        return result;
    }

}
