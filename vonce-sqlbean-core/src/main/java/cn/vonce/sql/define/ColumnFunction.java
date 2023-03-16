package cn.vonce.sql.define;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 列字段lambda函数
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/7 19:16
 */
@FunctionalInterface
public interface ColumnFunction<T, R> extends Serializable, Function<T, R> {

}
