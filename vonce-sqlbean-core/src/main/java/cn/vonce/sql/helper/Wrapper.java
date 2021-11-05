package cn.vonce.sql.helper;

import cn.vonce.sql.enumerate.SqlLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件包装器
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2021年4月28日下午5:49:00
 */
public class Wrapper implements Serializable {

    private List<Data> dataList = new ArrayList<>();

    /**
     * 条件
     *
     * @param cond
     * @return
     */
    public static Wrapper where(Cond cond) {
        Wrapper wrapper = new Wrapper();
        wrapper.dataList.add(new Data(SqlLogic.AND, cond));
        return wrapper;
    }

    /**
     * 条件
     *
     * @param cond
     * @return
     */
    public static Wrapper having(Cond cond) {
        return where(cond);
    }

    /**
     * 并且
     *
     * @param wrapper
     * @return
     */
    public Wrapper and(Wrapper wrapper) {
        dataList.add(new Data(SqlLogic.AND, wrapper));
        return this;
    }

    /**
     * 并且
     *
     * @param cond
     * @return
     */
    public Wrapper and(Cond cond) {
        dataList.add(new Data(SqlLogic.AND, cond));
        return this;
    }

    /**
     * 或者
     *
     * @param wrapper
     * @return
     */
    public Wrapper or(Wrapper wrapper) {
        dataList.add(new Data(SqlLogic.OR, wrapper));
        return this;
    }

    /**
     * 或者
     *
     * @param cond
     * @return
     */
    public Wrapper or(Cond cond) {
        dataList.add(new Data(SqlLogic.OR, cond));
        return this;
    }

    /**
     * 获得条件模型列表
     *
     * @return
     */
    public List<Data> getDataList() {
        return this.dataList;
    }

    /**
     * 条件模型
     */
    public static class Data implements Serializable {
        private SqlLogic sqlLogic;
        private Object item;

        public Data() {
        }

        public Data(SqlLogic sqlLogic, Object item) {
            this.sqlLogic = sqlLogic;
            this.item = item;
        }

        public SqlLogic getSqlLogic() {
            return sqlLogic;
        }

        public void setSqlLogic(SqlLogic sqlLogic) {
            this.sqlLogic = sqlLogic;
        }

        public Object getItem() {
            return item;
        }

        public void setItem(Object item) {
            this.item = item;
        }
    }

}
