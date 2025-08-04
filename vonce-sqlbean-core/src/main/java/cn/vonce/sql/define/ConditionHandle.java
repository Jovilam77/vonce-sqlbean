package cn.vonce.sql.define;

import cn.vonce.sql.bean.Condition;

import java.io.Serializable;

/**
 * Lambda条件处理
 *
 * @author Jovi《imjovi@qq.com》
 * @version 1.0《2025/7/29 16:03》
 */
@FunctionalInterface
public interface ConditionHandle<Action> extends Serializable {

    /**
     * 处理
     *
     * @param condition
     * @return
     */
    void handle(Condition<Action> condition);

}
