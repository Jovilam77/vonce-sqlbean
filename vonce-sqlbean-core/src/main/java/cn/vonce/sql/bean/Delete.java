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

}
