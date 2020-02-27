package cn.vonce.sql.annotation;

import java.lang.annotation.*;

/**
 * 标识实体类对应的id字段
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 22:22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlBeanId {

    /**
     * NORMAL：常规的，手写id
     * AUTO：自增长id
     * UUID：长度32位的uuid
     * SNOWFLAKE_ID_18：长度18位的唯一有序id（64bits JavaScript会丢失精度，只能当作String类型处理）
     * SNOWFLAKE_ID_16：长度16位的唯一有序id（53bits JavaScript能正常处理）
     */
    public enum GenerateType {
        NORMAL, AUTO, UUID, SNOWFLAKE_ID_16, SNOWFLAKE_ID_18
    }

    GenerateType generate() default GenerateType.NORMAL;

}
