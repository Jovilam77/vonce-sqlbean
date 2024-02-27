package cn.vonce.sql.bean;

/**
 * Group By
 */
public class Group {

    private Column column;

    public Group() {
    }

    public Group(Column column) {
        this.column = column;
    }

    public Group(String tableAlias, String name) {
        column = new Column(tableAlias, name, "");
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
}
