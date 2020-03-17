package cn.vonce.sql.annotation;

import cn.vonce.sql.enumerate.JoinType;

import java.lang.annotation.*;

/**
 * JoinType
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月21日上午12:00:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlBeanJoin {

    /**
     * 字段名称
     *
     * @return
     */
    String[] value() default "";

    /**
     * 是否为一个实体类
     *
     * @return
     */
    boolean isBean() default false;

    /**
     * 连接类型
     *
     * @return
     */
    JoinType type() default JoinType.INNER_JOIN;

    /**
     * 连接的schema
     *
     * @return
     */
    String schema() default "";

    /**
     * 连接的表名
     *
     * @return
     */
    String table() default "";

    /**
     * 连接表的别名
     *
     * @return
     */
    String tableAlias() default "";

    /**
     * 连接的表字段
     *
     * @return
     */
    String tableKeyword() default "";

    /**
     * 主表的字段
     *
     * @return
     */
    String mainKeyword();

}
