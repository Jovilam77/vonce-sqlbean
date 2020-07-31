package cn.vonce.sql.annotation;

import cn.vonce.sql.enumerate.JdbcType;

import java.lang.annotation.*;

/**
 * 标识Bean 实体类的字段与数据库中对应的字段名
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年2月8日下午2:55:04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlColumn {

    /**
     * 列字段名称
     *
     * @return
     */
    String value();

    /**
     * 不是null
     *
     * @return
     */
    boolean notNull() default false;

    /**
     * 类型
     *
     * @return
     */
    JdbcType type() default JdbcType.NULL;

    /**
     * 长度
     *
     * @return
     */
    int length() default 0;

    /**
     * 小数点
     *
     * @return
     */
    int decimal() default 0;

    /**
     * 默认值
     *
     * @return
     */
    String def() default "";

    /**
     * 是否忽略该字段
     *
     * @return
     */
    boolean ignore() default false;

}
