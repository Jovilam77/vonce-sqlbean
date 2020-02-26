package cn.vonce.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将标注的实体类生成对应表的字段常量
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/2/26 14:22
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface SqlBeanCons {


}
