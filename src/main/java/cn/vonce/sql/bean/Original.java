package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 原始的值（既不会对它进行任何处理，传入什么即是什么）
 */
public class Original implements Serializable {

    public Original(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Original{" +
                "value='" + value + '\'' +
                '}';
    }
}
