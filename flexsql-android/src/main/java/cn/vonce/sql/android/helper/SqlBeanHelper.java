package cn.vonce.sql.android.helper;

import cn.vonce.sql.android.service.SqlBeanServiceImpl;

/**
 * SqlBean数据库助手
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2022/4/16 0:35
 */
public class SqlBeanHelper<T, ID> extends SqlBeanServiceImpl<T, ID> {

    public SqlBeanHelper(Class<?> clazz, DatabaseHelper databaseHelper) {
        super(clazz, databaseHelper);
    }

}
