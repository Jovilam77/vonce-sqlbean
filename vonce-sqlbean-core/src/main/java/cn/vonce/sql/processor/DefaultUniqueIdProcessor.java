package cn.vonce.sql.processor;

import cn.vonce.sql.enumerate.IdType;
import cn.vonce.sql.uitls.IdBuilder;
import cn.vonce.sql.uitls.SnowflakeId16;
import cn.vonce.sql.uitls.SnowflakeId18;

import java.util.UUID;

/**
 * 唯一id处理器 默认实现
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/28 15:42
 */
public class DefaultUniqueIdProcessor implements UniqueIdProcessor {

    @Override
    public Object uniqueId(IdType idType) {
        switch (idType) {
            case UUID:
                return IdBuilder.uuid();
            case SNOWFLAKE_ID_16:
                return IdBuilder.snowflake16();
            case SNOWFLAKE_ID_18:
                return IdBuilder.snowflake18();
            default:
                return null;
        }
    }

}
