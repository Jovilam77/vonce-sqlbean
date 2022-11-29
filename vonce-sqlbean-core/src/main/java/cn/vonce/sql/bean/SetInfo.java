package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * Update Set 对象
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/29 11:08
 */
public class SetInfo extends Column implements Serializable {

    public SetInfo() {
        super();
    }

    public SetInfo(String columnName, Object value) {
        this(null, columnName, value);
    }

    public SetInfo(String tableAlias, String columnName, Object value) {
        super(tableAlias, columnName, "");
        this.value = value;
    }

    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
