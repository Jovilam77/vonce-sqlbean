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

    public enum Operator {
        EQUAL, ADDITION, SUBTRACT
    }

    public SetInfo() {
        super();
    }

    public SetInfo(String columnName, Object value) {
        this(null, columnName, value);
    }

    public SetInfo(String tableAlias, String columnName, Object value) {
        super(tableAlias, columnName, "");
        this.operator = Operator.EQUAL;
        this.value = value;
    }

    public SetInfo(Operator operator, String columnName, Object value1, Object value2) {
        this(operator, null, columnName, value1, value2);
    }

    public SetInfo(Operator operator, String tableAlias, String columnName, Object value1, Object value2) {
        super(tableAlias, columnName, "");
        this.operator = operator;
        this.value = new Object[]{value1, value2};
    }

    private Operator operator;
    private Object value;

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
