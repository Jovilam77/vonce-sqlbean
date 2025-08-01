package cn.vonce.sql.bean;

import cn.vonce.sql.define.ConditionHandle;
import cn.vonce.sql.enumerate.SqlLogic;
import java.io.Serializable;

/**
 * 条件逻辑
 *
 * @param <Action>
 */
public class Logic<Action> implements Serializable {

    private Condition<Action> condition;

    private Logic() {
    }

    protected Logic(Condition condition) {
        this.condition = condition;
    }

    /**
     * 并且
     *
     * @param
     * @return
     */
    public Condition<Action> and() {
        condition.setSqlLogic(SqlLogic.AND);
        return condition;
    }

    /**
     * 并且
     *
     * @param
     * @return
     */
    public Logic<Action> and(ConditionHandle<Action> cond) {
        Condition<Action> result = new Condition<>(condition.getAction());
        cond.handle(result);
        condition.getDataList().add(new ConditionData(SqlLogic.AND, result.getDataList()));
        return this;
    }

    /**
     * 或者
     *
     * @param
     * @return
     */
    public Condition<Action> or() {
        condition.setSqlLogic(SqlLogic.OR);
        return condition;
    }

    /**
     * 或者
     *
     * @param
     * @return
     */
    public Logic<Action> or(ConditionHandle<Action> cond) {
        Condition<Action> result = new Condition<>();
        cond.handle(result);
        condition.getDataList().add(new ConditionData(SqlLogic.OR, result.getDataList()));
        return this;
    }

    /**
     * 返回Bean对象
     *
     * @return
     */
    public Action back() {
        return condition.getAction();
    }

}
