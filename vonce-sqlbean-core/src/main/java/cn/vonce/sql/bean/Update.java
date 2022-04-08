package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 更新
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Update<T> extends Condition implements Serializable {

    //更新的实体对象
    private T updateBean = null;
    //需要过滤的字段
    private String[] filterFields = null;
    //默认只更新不为空的字段
    private boolean updateNotNull = true;
    //是否使用乐观锁
    private boolean optimisticLock = false;
    //是否为逻辑删除
    private boolean logicallyDelete = false;
    private SimpleCondition<Update<T>> whereCondition = new SimpleCondition<>(this);

    /**
     * 是否只更新不为null的字段
     *
     * @return
     */
    public boolean isUpdateNotNull() {
        return updateNotNull;
    }

    /**
     * 设置是否只更新不为null的字段
     *
     * @param updateNotNull
     */
    public void setUpdateNotNull(boolean updateNotNull) {
        this.updateNotNull = updateNotNull;
    }

    /**
     * 是否使用乐观锁
     *
     * @return
     */
    public boolean isOptimisticLock() {
        return optimisticLock;
    }

    /**
     * 设置是否使用乐观锁
     *
     * @param optimisticLock
     */
    public void setOptimisticLock(boolean optimisticLock) {
        this.optimisticLock = optimisticLock;
    }

    /**
     * 获取更新实体类
     *
     * @return
     */
    public T getUpdateBean() {
        return updateBean;
    }

    /**
     * 设置更新实体类
     *
     * @param updateBean
     */
    public void setUpdateBean(T updateBean) {
        this.updateBean = updateBean;
    }

    /**
     * 获取过滤的字段
     *
     * @return
     */
    public String[] getFilterFields() {
        return filterFields;
    }

    /**
     * 设置过滤的字段
     *
     * @param filterField
     */
    public void setFilterFields(String... filterField) {
        this.filterFields = filterField;
    }

    public boolean isLogicallyDelete() {
        return logicallyDelete;
    }

    public void setLogicallyDelete(boolean logicallyDelete) {
        this.logicallyDelete = logicallyDelete;
    }

    public SimpleCondition<Update<T>> where() {
        return whereCondition;
    }

}