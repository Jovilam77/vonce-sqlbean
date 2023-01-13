package cn.vonce.sql.define;

import cn.vonce.sql.bean.Condition;

/**
 * 表连接条件
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/30 11:44
 */
public interface JoinOn {

    void on(Condition condition);

}
