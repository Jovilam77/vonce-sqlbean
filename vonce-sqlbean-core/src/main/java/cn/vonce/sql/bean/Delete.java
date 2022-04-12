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
public class Delete extends CommonCondition<Delete> implements Serializable {

    public Delete() {
        super();
        super.setReturnObj(this);
    }

}
