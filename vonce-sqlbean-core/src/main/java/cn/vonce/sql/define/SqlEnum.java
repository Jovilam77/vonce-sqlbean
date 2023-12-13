package cn.vonce.sql.define;

import java.io.Serializable;

/**
 * 枚举基类
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2023/6/30 11:27
 */
public interface SqlEnum<T extends Serializable> {

    /**
     * 枚举类名
     *
     * @return
     */
    default String getName() {
        return this.toString();
    }

    /**
     * 代码
     *
     * @return
     */
    T getCode();

    /**
     * 描述
     *
     * @return
     */
    String getDesc();

}
