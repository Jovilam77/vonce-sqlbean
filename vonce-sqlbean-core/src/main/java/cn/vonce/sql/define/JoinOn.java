package cn.vonce.sql.define;

import cn.vonce.sql.bean.Condition;
import cn.vonce.sql.bean.Join;
import cn.vonce.sql.bean.Select;

/**
 * 表连接条件
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/11/30 11:44
 */
public interface JoinOn {

    Condition<Select> on(Join join);

}
