package cn.vonce.sql.bean;

import cn.vonce.sql.annotation.SqlBeanTable;
import java.io.Serializable;

/**
 * Delete
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Delete extends Common implements Serializable {

    private String deleteTable = "";//删除哪张表

    /**
     * 获取删除表名
     *
     * @return
     * @author Jovi
     * @date 2017年8月18日上午8:54:53
     */
    public String getDeleteBable() {
        return deleteTable;
    }

    /**
     * 设置删除表名
     *
     * @param deleteBable
     * @author Jovi
     * @date 2017年8月18日上午8:54:41
     */
    public void setDeleteBable(String deleteBable) {
        this.deleteTable = deleteBable;
    }

    /**
     * 设置删除表名
     *
     * @param clazz
     * @author Jovi
     * @date 2018年3月30日上午10:38:39
     */
    public void setDeleteBable(Class<?> clazz) {
        SqlBeanTable sqlBeanTable = clazz.getAnnotation(SqlBeanTable.class);
        if (sqlBeanTable != null) {
            this.deleteTable = sqlBeanTable.value();
        } else {
            this.deleteTable = clazz.getSimpleName();
        }
    }

}
