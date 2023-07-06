package cn.vonce.sql.bean;

import cn.vonce.sql.define.ColumnFun;
import cn.vonce.sql.enumerate.JoinType;
import java.io.Serializable;

/**
 * Select
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019年6月10日上午12:00:00
 */
public class Join implements Serializable {

    public Join() {
    }

    public Join(JoinType joinType, String schema, String tableName, String tableAlias) {
        this.joinType = joinType;
        this.schema = schema;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
    }

    public Join(JoinType joinType, String schema, String tableName, String tableAlias, String tableKeyword, String mainKeyword, String on) {
        this.joinType = joinType;
        this.schema = schema;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableKeyword = tableKeyword;
        this.mainKeyword = mainKeyword;
        this.on = on;
    }

    private JoinType joinType;
    private String schema;
    private String tableName;
    private String tableAlias;
    @Deprecated
    private String tableKeyword;
    @Deprecated
    private String mainKeyword;
    @Deprecated
    private String on;
    /**
     * 链式返回对象
     */
    private Select returnObj;
    /**
     * Join 条件
     */
    private Condition<Select> joinCondition;

    protected void setReturnObj(Select returnObj) {
        this.returnObj = returnObj;
        joinCondition = new Condition<>(returnObj);
    }


    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    @Deprecated
    public String getTableKeyword() {
        return tableKeyword;
    }

    @Deprecated
    public void setTableKeyword(String tableKeyword) {
        this.tableKeyword = tableKeyword;
    }

    @Deprecated
    public String getMainKeyword() {
        return mainKeyword;
    }

    @Deprecated
    public void setMainKeyword(String mainKeyword) {
        this.mainKeyword = mainKeyword;
    }

    @Deprecated
    public String getOn() {
        return on;
    }

    @Deprecated
    public void setOn(String on) {
        this.on = on;
    }

    public Condition<Select> on(String field, Object value) {
        joinCondition.eq(field, value);
        return joinCondition;
    }

    public Condition<Select> on(String tableAlias, String field, Object value) {
        joinCondition.eq(tableAlias, field, value);
        return joinCondition;
    }

    public Condition<Select> on(Column column, Object value) {
        joinCondition.eq(column, value);
        return joinCondition;
    }

    public <T, R> Condition<Select> on(ColumnFun<T, R> columnFun, Object value) {
        joinCondition.eq(columnFun, value);
        return joinCondition;
    }

    public Condition<Select> on() {
        return joinCondition;
    }

    @Override
    public String toString() {
        return "Join{" +
                "joinType=" + joinType +
                ", schema='" + schema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableAlias='" + tableAlias + '\'' +
                ", on='" + on + '\'' +
                '}';
    }
}
