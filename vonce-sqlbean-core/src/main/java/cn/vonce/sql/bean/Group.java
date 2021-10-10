package cn.vonce.sql.bean;

/**
 * Group By
 */
public class Group extends Column {

    public Group() {
        super();
    }

    public Group(String tableAlias, String name) {
        super(tableAlias, name, "");
    }

}
