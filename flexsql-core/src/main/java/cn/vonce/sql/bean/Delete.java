package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 删除
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Delete extends CommonCondition<Delete> implements Serializable {

    /**
     * 是否为逻辑删除(优先走逻辑删除,该属性为true且实体类存在有@SqlLogically注解才走逻辑删除)
     */
    private boolean logicallyDelete = true;

    public Delete() {
        super();
        super.setReturnObj(this);
    }

    /**
     * 获取是否为逻辑删除
     *
     * @return
     */
    public boolean isLogicallyDelete() {
        return logicallyDelete;
    }

    /**
     * 设置是否为逻辑删除
     *
     * @param logicallyDelete
     */
    public void setLogicallyDelete(boolean logicallyDelete) {
        this.logicallyDelete = logicallyDelete;
    }

    /**
     * 设置table
     *
     * @param name
     */
    public Delete table(String name) {
        super.setTable(name, name);
        return this;
    }

    /**
     * 设置table
     *
     * @param name
     * @param aliasName
     */
    public Delete table(String name, String aliasName) {
        super.setTable(name, aliasName);
        return this;
    }

    /**
     * 设置table
     *
     * @param name
     * @param aliasName
     */
    public Delete table(String schema, String name, String aliasName) {
        super.setTable(schema, name, aliasName);
        return this;
    }

    /**
     * 设置table sql 内容
     *
     * @param clazz 表对应的实体类
     */
    public Delete table(Class<?> clazz) {
        super.setTable(clazz);
        return this;
    }

}
