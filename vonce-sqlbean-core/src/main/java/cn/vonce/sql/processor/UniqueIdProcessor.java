package cn.vonce.sql.processor;

import cn.vonce.sql.enumerate.IdType;

/**
 * 唯一id处理器接口
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 15:31
 */
public interface UniqueIdProcessor {

    /**
     * 生成唯一id
     *
     * @param idType
     * @return
     */
    Object uniqueId(IdType idType);

}
