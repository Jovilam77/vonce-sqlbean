package cn.vonce.sql.bean;

/**
 * 备份表和数据（根据条件备份表和数据到一张指定名称的新表）
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2021年6月21日下午21:53:00
 */
public class Backup extends CommonCondition<Backup> {

    public Backup() {
        super();
        super.setReturnObj(this);
    }

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
