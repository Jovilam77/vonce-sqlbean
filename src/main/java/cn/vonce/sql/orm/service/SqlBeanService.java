package cn.vonce.sql.orm.service;

/**
 * 通用的业务接口
 *
 * @param <T>
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2018年5月15日下午3:57:33
 */
public interface SqlBeanService<T> extends SelectService<T>, InsertService<T>, UpdateService<T>, DeleteService {


}
