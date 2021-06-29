package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 插入
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Insert<T> extends Common implements Serializable {

    private T[] insertBean = null;//插入的实体对象

    /**
     * 获取插入实体类
     *
     * @return
     */
    public T[] getInsertBean() {
        return insertBean;
    }

    /**
     * 设置插入实体类
     *
     * @param insertBean
     */
    public void setInsertBean(T... insertBean) {
        this.insertBean = insertBean;
    }

}