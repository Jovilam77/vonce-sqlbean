package cn.vonce.sql.bean;

import cn.vonce.sql.enumerate.AlterDifference;
import cn.vonce.sql.enumerate.AlterType;

import java.util.List;

/**
 * 改变表结构
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/9/22 16:46
 */
public class Alter extends Common {

    /**
     * 改变类型
     */
    private AlterType type;
    /**
     * 字段信息
     */
    private ColumnInfo columnInfo;
    /**
     * 旧字段名称
     */
    private String oldColumnName;
    /**
     * 排序在哪个后面
     */
    private String afterColumnName;

    /**
     * 修改的差异
     */
    private List<AlterDifference> differences;

    public AlterType getType() {
        return type;
    }

    public void setType(AlterType type) {
        this.type = type;
    }

    public ColumnInfo getColumnInfo() {
        return columnInfo;
    }

    public void setColumnInfo(ColumnInfo columnInfo) {
        this.columnInfo = columnInfo;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public void setOldColumnName(String oldColumnName) {
        this.oldColumnName = oldColumnName;
    }

    public String getAfterColumnName() {
        return afterColumnName;
    }

    public void setAfterColumnName(String afterColumnName) {
        this.afterColumnName = afterColumnName;
    }

    public List<AlterDifference> getDifferences() {
        return differences;
    }

    public void setDifferences(List<AlterDifference> differences) {
        this.differences = differences;
    }

}
