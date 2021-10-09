package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 原始的值（既不会对它进行任何处理，传入什么即是什么）
 */
public class Original implements Serializable {

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
