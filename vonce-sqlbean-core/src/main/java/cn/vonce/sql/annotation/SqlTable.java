package cn.vonce.sql.annotation;

import java.lang.annotation.*;

/**
 * 标识Bean 实体类对应的表名
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年2月8日下午2:54:20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SqlTable {

    /**
     * 如果表不存在则自动创建表
     *
     * @return
     */
    boolean autoCreate() default true;

    /**
     * 是否自动更改表结构
     *
     * @return
     */
    boolean autoAlter() default false;

    /**
     * 生成实体类生成对应表的字段常量
     *
     * @return
     */
    boolean constant() default true;

    /**
     * map-underscore-to-camel-case
     * 是否开启Java字段驼峰命名转Sql字段下划线命名
     *
     * @return
     */
    boolean mapUsToCc() default true;

    /**
     * 是否为视图
     *
     * @return
     */
    boolean isView() default false;

    /**
     * 表名
     *
     * @return
     */
    String value();

    /**
     * 表别名
     *
     * @return
     */
    String alias() default "";

    /**
     * schema
     *
     * @return
     */
    String schema() default "";

    /**
     * 表注释
     *
     * @return
     */
    String remarks() default "";

}
