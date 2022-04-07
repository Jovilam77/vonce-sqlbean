package cn.vonce.sql.bean;

import java.io.Serializable;

/**
 * 删除
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2017年8月18日上午9:00:19
 */
public class Delete extends Condition implements Serializable {

    private SimpleCondition<Delete> whereSimpleCondition = new SimpleCondition<>(this);

    public SimpleCondition<Delete> where() {
        return whereSimpleCondition;
    }

}
