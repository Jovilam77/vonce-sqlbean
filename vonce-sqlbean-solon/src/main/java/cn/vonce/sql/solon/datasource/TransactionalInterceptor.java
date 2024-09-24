package cn.vonce.sql.solon.datasource;

import cn.vonce.sql.java.annotation.DbTransactional;
import cn.vonce.sql.java.datasource.ConnectionContextHolder;
import cn.vonce.sql.java.datasource.TransactionalContextHolder;
import cn.vonce.sql.uitls.IdBuilder;
import cn.vonce.sql.uitls.StringUtil;
import org.noear.solon.core.aspect.Invocation;
import org.noear.solon.core.aspect.MethodInterceptor;

/**
 * 事务拦截器
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/14 11:34
 */
public class TransactionalInterceptor implements MethodInterceptor {

    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        DbTransactional dbTransactional = inv.getMethodAnnotation(DbTransactional.class);
        if (inv.method().getMethod().isAnnotationPresent(DbTransactional.class)) {
            dbTransactional = inv.method().getMethod().getAnnotation(DbTransactional.class);
        }
        Object result;
        try {
            String xid = TransactionalContextHolder.getXid();
            //已经存在事务则加入事务并执行
            if (StringUtil.isNotBlank(xid)) {
                result = inv.invoke();
                return result;
            }
            //当前没有事务则创建事务
            else {
                if (dbTransactional.readOnly()) {
                    ConnectionContextHolder.setReadOnly(true);
                }
                TransactionalContextHolder.setXid(IdBuilder.uuid());
                result = inv.invoke();
                //移除事务id
                TransactionalContextHolder.clearXid();
                //提交或回滚事务
                ConnectionContextHolder.commitOrRollback(true);
            }
        } catch (Throwable e) {
            //移除事务id
            TransactionalContextHolder.clearXid();
            Class<? extends Throwable>[] rollbackFor = dbTransactional.rollbackFor();
            Class<? extends Throwable>[] noRollbackFor = dbTransactional.noRollbackFor();
            boolean needRollback = false;
            if (rollbackFor.length > 0) {
                //遇到哪些异常回滚
                for (Class<? extends Throwable> thr : rollbackFor) {
                    if (thr.isAssignableFrom(e.getClass())) {
                        needRollback = true;
                        break;
                    }
                }
            }
            if (noRollbackFor.length > 0) {
                //遇到哪些异常不回滚
                for (Class<? extends Throwable> thr : noRollbackFor) {
                    if (thr.isAssignableFrom(e.getClass())) {
                        needRollback = false;
                        break;
                    }
                }
            }
            if (rollbackFor.length == 0 && noRollbackFor.length == 0) {
                //回滚事务
                needRollback = true;
            }
            ConnectionContextHolder.commitOrRollback(!needRollback);
            throw e;
        }
        return result;
    }

}
