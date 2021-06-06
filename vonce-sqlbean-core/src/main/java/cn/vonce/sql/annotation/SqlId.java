package cn.vonce.sql.annotation;

import cn.vonce.sql.enumerate.IdType;

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
public @interface SqlId {

    IdType type() default IdType.NORMAL;

}
