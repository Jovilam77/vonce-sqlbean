package cn.vonce.sql.service;

import cn.vonce.sql.config.SqlBeanMeta;

/**
 * 通用的业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2018年5月15日下午3:57:33
 */
public interface SqlBeanService<T, ID> extends SelectService<T, ID>, InsertService<T>, UpdateService<T, ID>, DeleteService<ID> {

    /**
     * 获取Bean类型
     *
     * @return
     */
    Class<?> getBeanClass();

    /**
     * 获得数据库相关信息
     *
     * @return
     */
    SqlBeanMeta getSqlBeanMeta();

}
