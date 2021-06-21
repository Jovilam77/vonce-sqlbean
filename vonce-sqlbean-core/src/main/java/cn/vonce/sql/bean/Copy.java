package cn.vonce.sql.bean;

/**
 * 复制表数据
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2021年6月21日下午21:53:00
 */
public class Copy extends  Condition{

    private String targetSchema;
    private String targetTableName;
    private Column[] columns;

    public String getTargetSchema() {
        return targetSchema;
    }

    public void setTargetSchema(String targetSchema) {
        this.targetSchema = targetSchema;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }

    public Column[] getColumns() {
        return columns;
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

}
