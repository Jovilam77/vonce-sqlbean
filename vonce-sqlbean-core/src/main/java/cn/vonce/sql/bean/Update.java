package cn.vonce.sql.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 更新
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Update<T> extends CommonCondition<Update<T>> implements Serializable {

    public Update() {
        super();
        super.setReturnObj(this);
    }

    /**
     * 更新的实体对象
     */
    private T updateBean = null;
    /**
     * 需要过滤的字段
     */
    private String[] filterFields = null;
    /**
     * 默认只更新不为空的字段
     */
    private boolean updateNotNull = true;
    /**
     * 是否使用乐观锁
     */
    private boolean optimisticLock = false;

    /**
     * 更新的字段列表
     */
    private List<SetInfo> setInfoList = new ArrayList<>();

    public T getUpdateBean() {
        return updateBean;
    }

    public void setUpdateBean(T updateBean) {
        this.updateBean = updateBean;
    }

    public String[] getFilterFields() {
        return filterFields;
    }

    public void setFilterFields(String... filterFields) {
        this.filterFields = filterFields;
    }

    public boolean isUpdateNotNull() {
        return updateNotNull;
    }

    public void setUpdateNotNull(boolean updateNotNull) {
        this.updateNotNull = updateNotNull;
    }

    public boolean isOptimisticLock() {
        return optimisticLock;
    }

    public void setOptimisticLock(boolean optimisticLock) {
        this.optimisticLock = optimisticLock;
    }

    public List<SetInfo> getSetInfoList() {
        return setInfoList;
    }

    public void setSetInfoList(List<SetInfo> setInfoList) {
        this.setInfoList = setInfoList;
    }

    public Update<T> set(String columnName, Object value) {
        setInfoList.add(new SetInfo(columnName, value));
        return this;
    }

    public Update<T> set(String tableAlias, String columnName, Object value) {
        setInfoList.add(new SetInfo(tableAlias, columnName, value));
        return this;
    }

    public Update<T> set(Column column, Object value) {
        setInfoList.add(new SetInfo(column.getTableAlias(), column.getName(), value));
        return this;
    }

    public static void main(String[] args) {

        new Update<>().set("", "").set("", 1).where().eq("", 1);
    }

}