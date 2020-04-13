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
@Inherited
public @interface SqlTable {

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

}
