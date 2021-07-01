package cn.vonce.sql.spring.annotation;

import java.lang.annotation.*;

/**
 * 动态Schema
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/6/23 23:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface DbDynSchema {

}
