package cn.vonce.sql.java.config;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 注册自动配置多数据源
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/9/24 17:12
 */
public interface RegisterAutoConfigMultiDataSourceBean {

    /**
     * 注册bean
     *
     * @param defaultTargetDataSource
     * @param dataSourceMap
     */
    void registerBean(DataSource defaultTargetDataSource, Map<String, DataSource> dataSourceMap);

}
