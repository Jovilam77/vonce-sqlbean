package cn.vonce.sql.solon.datasource;

import cn.vonce.sql.java.annotation.DbDynSchema;
import cn.vonce.sql.provider.DynSchemaContextHolder;
import org.noear.solon.core.aspect.Interceptor;
import org.noear.solon.core.aspect.Invocation;

/**
 * 动态Schema切换切点
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/6/23 23:32
 */
public abstract class AbstractDynSchemaInterceptor implements Interceptor {

    public abstract String getSchema();

    @Override
    public Object doIntercept(Invocation inv) throws Throwable {
        Class<?> clazz = inv.target().getClass();
        if (clazz.isAnnotationPresent(DbDynSchema.class)) {
            DynSchemaContextHolder.setSchema(getSchema());
        }
        Object result = inv.invoke();
        DynSchemaContextHolder.clearSchema();
        return result;
    }

}

