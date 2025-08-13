package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.SqlLogic;

import java.io.Serializable;

/**
 * 条件模型
 */
public class ConditionData implements Serializable {
    private SqlLogic sqlLogic;
    private Object item;

    private ConditionData() {
    }

    public ConditionData(SqlLogic sqlLogic, Object item) {
        this.sqlLogic = sqlLogic;
        this.item = item;
    }

    public SqlLogic getSqlLogic() {
        return sqlLogic;
    }

    public void setSqlLogic(SqlLogic sqlLogic) {
        this.sqlLogic = sqlLogic;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}