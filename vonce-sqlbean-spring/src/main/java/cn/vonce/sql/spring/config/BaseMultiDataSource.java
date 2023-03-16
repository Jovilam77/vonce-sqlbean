package cn.vonce.sql.spring.config;

/**
 * 多数据源接口
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2022/12/12 19:47
 */
public interface BaseMultiDataSource {

    /**
     * 是否为默认数据源
     *
     * @return
     */
    boolean defaultDataSource();

}
