package cn.vonce.sql.annotation;

import java.lang.annotation.*;

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
@Documented
public @interface SqlConstant {



}
