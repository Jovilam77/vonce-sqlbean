package cn.vonce.sql.uitls;

import cn.vonce.sql.bean.Column;
import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.exception.SqlBeanException;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Lambda工具类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/7 19:18
 */
public class LambdaUtil {

    /**
     * 获取列字段对象
     *
     * @param column
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Column getColumn(ColumnFun<T, R> column) {
        if (SqlBeanUtil.isAndroidEnv()){
            throw new SqlBeanException("Android环境不支持Lambda表达式指定列字段(XXXClass::getXXX)");
        }
        SerializedLambda lambda = null;
        try {
            Method method = column.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(column);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return SqlBeanUtil.getColumnByLambda(lambda);
    }

}
