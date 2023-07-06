package cn.vonce.sql.helper;

import cn.vonce.sql.bean.ConditionData;
import cn.vonce.sql.enumerate.SqlLogic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件包装器
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021年4月28日下午5:49:00
 */
public class Wrapper implements Serializable {

    private List<ConditionData> dataList = new ArrayList<>();

    /**
     * 条件
     *
     * @param cond
     * @return
     */
    public static Wrapper where(Cond cond) {
        Wrapper wrapper = new Wrapper();
        wrapper.dataList.add(new ConditionData(SqlLogic.AND, cond));
        return wrapper;
    }

    /**
     * 条件
     *
     * @param cond
     * @return
     */
    public static Wrapper where(Wrapper cond) {
        Wrapper wrapper = new Wrapper();
        wrapper.dataList.add(new ConditionData(SqlLogic.AND, cond));
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
        dataList.add(new ConditionData(SqlLogic.AND, wrapper));
        return this;
    }

    /**
     * 并且
     *
     * @param cond
     * @return
     */
    public Wrapper and(Cond cond) {
        dataList.add(new ConditionData(SqlLogic.AND, cond));
        return this;
    }

    /**
     * 或者
     *
     * @param wrapper
     * @return
     */
    public Wrapper or(Wrapper wrapper) {
        dataList.add(new ConditionData(SqlLogic.OR, wrapper));
        return this;
    }

    /**
     * 或者
     *
     * @param cond
     * @return
     */
    public Wrapper or(Cond cond) {
        dataList.add(new ConditionData(SqlLogic.OR, cond));
        return this;
    }

    /**
     * 获得条件模型列表
     *
     * @return
     */
    public List<ConditionData> getDataList() {
        return this.dataList;
    }

}
