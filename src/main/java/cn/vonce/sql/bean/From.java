package cn.vonce.sql.bean;

/**
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019/9/27 11:49
 */
public class From {

    public From() {

    }

    public From(String name) {
        this.name = name;
    }

    public From(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    private String name;

    private String alias;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
