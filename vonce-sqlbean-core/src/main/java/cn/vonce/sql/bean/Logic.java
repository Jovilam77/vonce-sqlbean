package cn.vonce.sql.bean;


import cn.vonce.sql.enumerate.SqlLogic;

import java.io.Serializable;

public class Logic<Action> implements Serializable {

    private SimpleCondition condition;

    private Logic() {
    }

    protected Logic(SimpleCondition condition) {
        this.condition = condition;
    }

    /**
     * 并且
     *
     * @param
     * @return
     */
    public SimpleCondition and() {
        condition.setSqlLogic(SqlLogic.AND);
        return condition;
    }

    /**
     * 或者
     *
     * @param
     * @return
     */
    public SimpleCondition or() {
        condition.setSqlLogic(SqlLogic.OR);
        return condition;
    }

    public Action back() {
        return (Action) condition.getAction();
    }

}
