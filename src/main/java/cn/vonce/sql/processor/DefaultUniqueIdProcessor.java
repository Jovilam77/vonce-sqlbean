package cn.vonce.sql.processor;

import cn.vonce.common.utils.RandomUtil;
import cn.vonce.common.utils.SnowflakeId16;
import cn.vonce.common.utils.SnowflakeId18;
import cn.vonce.sql.enumerate.GenerateType;

/**
 * 唯一id处理器 默认实现
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 15:42
 */
public class DefaultUniqueIdProcessor implements UniqueIdProcessor {

    private final SnowflakeId18 snowflakeId18 = new SnowflakeId18(0);

    @Override
    public Object uniqueId(GenerateType generateType) {
        switch (generateType) {
            case UUID:
                return RandomUtil.makeUUID();
            case SNOWFLAKE_ID_16:
                return SnowflakeId16.nextId();
            case SNOWFLAKE_ID_18:
                return snowflakeId18.nextId();
            default:
                return null;
        }
    }
}
