package cn.vonce.sql.mapper;

/**
 * 结果集代理类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/6/14 15:21
 */
public class ResultSetDelegate<T> {

    public ResultSetDelegate(T delegate) {
        this.delegate = delegate;
    }

    private T delegate;

    public T getDelegate() {
        return delegate;
    }

}
