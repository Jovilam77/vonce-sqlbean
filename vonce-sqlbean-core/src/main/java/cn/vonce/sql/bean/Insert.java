package cn.vonce.sql.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 插入
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Insert<T> extends Common implements Serializable {

    private List<T> insertBean = null;//插入的实体对象

    /**
     * 获取插入实体类
     *
     * @return
     */
    public List<T> getInsertBean() {
        return insertBean;
    }

    /**
     * 设置插入实体类
     *
     * @param beanList
     */
    public void setInsertBean(List<T> beanList) {
        this.insertBean = beanList;
    }

    /**
     * 设置插入实体类
     *
     * @param bean
     */
    public void setInsertBean(T bean) {
        if (bean == null) {
            return;
        }
        if (this.insertBean == null) {
            this.insertBean = new ArrayList<>();
        }
        if (bean.getClass().isArray()) {
            T[] arrays = (T[]) bean;
            for (T item : arrays) {
                this.insertBean.add(item);
            }
        } else {
            this.insertBean.add(bean);
        }
    }

}