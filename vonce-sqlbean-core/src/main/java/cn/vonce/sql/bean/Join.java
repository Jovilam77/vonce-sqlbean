package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.JoinType;

import java.io.Serializable;

/**
 * Select
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月10日上午12:00:00
 */
public class Join implements Serializable {

    public Join() {
    }

    public Join(JoinType joinType, String schema, String tableName, String tableAlias, String tableKeyword, String mainKeyword) {
        this.joinType = joinType;
        this.schema = schema;
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableKeyword = tableKeyword;
        this.mainKeyword = mainKeyword;
    }

    private JoinType joinType;
    private String schema;
    private String tableName;
    private String tableAlias;
    private String tableKeyword;
    private String mainKeyword;

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

    public String getTableKeyword() {
        return tableKeyword;
    }

    public void setTableKeyword(String tableKeyword) {
        this.tableKeyword = tableKeyword;
    }

    public String getMainKeyword() {
        return mainKeyword;
    }

    public void setMainKeyword(String mainKeyword) {
        this.mainKeyword = mainKeyword;
    }

    @Override
    public String toString() {
        return "Join{" +
                "joinType=" + joinType +
                ", schema='" + schema + '\'' +
                ", tableName='" + tableName + '\'' +
                ", tableAlias='" + tableAlias + '\'' +
                ", tableKeyword='" + tableKeyword + '\'' +
                ", mainKeyword='" + mainKeyword + '\'' +
                '}';
    }
}
