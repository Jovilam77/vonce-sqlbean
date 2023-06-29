package cn.vonce.sql.bean;

import cn.vonce.sql.define.ColumnFun;

import java.io.Serializable;

/**
 * 原始的值（既不会对它进行任何处理，传入什么即是什么）
 */
public class Original implements Serializable {

    public <T, R> Original(ColumnFun<T, R> value) {
        this.value = value;
    }

    public Original(Object value) {
        this.value = value;
    }

    private Object value;

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Original{" +
                "value='" + value + '\'' +
                '}';
    }
}
