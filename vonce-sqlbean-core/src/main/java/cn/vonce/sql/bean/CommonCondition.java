package cn.vonce.sql.bean;

import cn.vonce.sql.helper.Wrapper;

/**
 * where条件
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2020年3月1日上午10:00:10
 */
public class CommonCondition<T> extends Common {

    /**
     * 链式返回对象
     */
    private T returnObj;
    /**
     * where 条件表达式 优先级一
     */
    private String where = "";
    /**
     * where 条件表达式参数
     */
    private Object[] agrs = null;
    /**
     * where 条件包装器 优先级二
     */
    private Wrapper whereWrapper;
    /**
     * where 条件 优先级三
     */
    private Condition<T> whereCondition;

    protected void setReturnObj(T returnObj) {
        this.returnObj = returnObj;
        whereCondition = new Condition<>(returnObj);
    }

    /**
     * 获取where sql 内容
     *
     * @return
     */
    public String getWhere() {
        return where;
    }

    /**
     * 设置where sql 内容
     *
     * @param where
     * @param args
     */
    public T where(String where, Object... args) {
        this.where = where;
        this.agrs = args;
        return returnObj;
    }

    /**
     * 获取where参数
     *
     * @return
     */
    public Object[] getAgrs() {
        return agrs;
    }

    public Condition<T> where() {
        return whereCondition;
    }

    /**
     * 获得where包装器
     *
     * @return
     */
    public Wrapper getWhereWrapper() {
        return whereWrapper;
    }

    /**
     * 设置Where条件包装器
     *
     * @param wrapper
     */
    public T where(Wrapper wrapper) {
        this.whereWrapper = wrapper;
        return returnObj;
    }

}
