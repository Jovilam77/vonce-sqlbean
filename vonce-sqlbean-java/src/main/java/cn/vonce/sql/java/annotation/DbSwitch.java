package cn.vonce.sql.java.annotation;

import cn.vonce.sql.java.enumerate.DbRole;

import java.lang.annotation.*;

/**
 * 选择数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/11/2 23:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface DbSwitch {

    DbRole value();

}
