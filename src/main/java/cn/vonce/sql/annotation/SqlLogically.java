package cn.vonce.sql.annotation;

import java.lang.annotation.*;

/**
 * 标记为逻辑删除字段
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/4/13 17:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface SqlLogically {


}
