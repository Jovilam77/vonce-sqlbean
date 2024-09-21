package cn.vonce.sql.android.mapper;

import android.database.Cursor;
import cn.vonce.sql.mapper.ResultSetDelegate;

/**
 * 行数据映射
 * @author Jovi
 *
 * @param <T>
 */
public interface RowMapper<T> {

    T mapRow(ResultSetDelegate<Cursor> resultSetDelegate, int index);

}
