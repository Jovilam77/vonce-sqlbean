package cn.vonce.sql.bean;

/**
 * 复制表数据（根据条件将数据复制插入到同样结构的表中）
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021年6月21日下午21:53:00
 */
public class Copy extends CommonCondition<Copy> {

    public Copy() {
        super();
        super.setReturnObj(this);
    }

    private String targetSchema;
    private String targetTableName;
    private Column[] columns;
    private Column[] targetColumns;

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

    public Column[] getTargetColumns() {
        return targetColumns;
    }

    public void setTargetColumns(Column[] targetColumns) {
        this.targetColumns = targetColumns;
    }

}
