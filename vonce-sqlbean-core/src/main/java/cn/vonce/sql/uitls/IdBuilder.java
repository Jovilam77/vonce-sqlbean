package cn.vonce.sql.uitls;

import java.util.UUID;

/**
 * Id创建者
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/6/21 11:41
 */
public class IdBuilder {

    /**
     * 生成uuid
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成ulid
     *
     * @return
     */
    public static String ulid() {
        return UlidCreator.getUlid().toString();
    }


    /**
     * 生成16位雪花id
     *
     * @return
     */
    public static long snowflake16() {
        return SnowflakeId16.nextId();
    }

    /**
     * 生成18位雪花id
     *
     * @return
     */
    public static long snowflake18() {
        return SnowflakeId18.instance().nextId();
    }

}
