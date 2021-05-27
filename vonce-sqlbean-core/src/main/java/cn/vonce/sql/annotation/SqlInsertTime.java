package cn.vonce.sql.annotation;

import java.lang.annotation.*;

/**
 * 标识Bean 实体类对应数据库中的插入时间，如果标注该注解，标注的字段为null将自动设置插入时间
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/5/27 23:03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlInsertTime {
}
