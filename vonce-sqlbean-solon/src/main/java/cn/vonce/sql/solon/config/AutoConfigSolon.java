package cn.vonce.sql.solon.config;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanMeta;
import cn.vonce.sql.java.annotation.DbSource;
import cn.vonce.sql.java.mapper.MybatisSqlBeanMapperInterceptor;
import cn.vonce.sql.solon.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.solon.datasource.DataSourceInterceptor;
import org.apache.ibatis.solon.MybatisAdapter;
import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Solon Mybatis 配置
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/8/12 15:27
 */
public class AutoConfigSolon implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanBuilderAdd(EnableAutoConfigMultiDataSource.class, new AutoConfigMultiDataSource());
        context.subWrapsOfType(DataSource.class, (bw) -> {
            init(bw);
        });

    }

    protected static void init(BeanWrap bw) {
        MybatisAdapter mybatisAdapter = MybatisAdapterManager.get(bw);
        if (mybatisAdapter != null) {
            mybatisAdapter.getConfiguration().addMapper(cn.vonce.sql.java.dao.MybatisSqlBeanDao.class);
            mybatisAdapter.getConfiguration().addInterceptor(new MybatisSqlBeanMapperInterceptor());
        }
        bw.context().beanMake(SolonAutoCreateTableListener.class);
        bw.context().beanInterceptorAdd(DbSource.class, new DataSourceInterceptor());
        try {
            Connection connection = mybatisAdapter.getConfiguration().getEnvironment().getDataSource().getConnection();
            SqlBeanConfig sqlBeanConfig = bw.context().getBean(SqlBeanConfig.class);
            SqlBeanMeta sqlBeanMeta = SqlBeanMeta.build(sqlBeanConfig, connection.getMetaData());
            connection.close();
            BeanWrap beanWrap = bw.context().wrap("sqlBeanMeta", sqlBeanMeta);
            bw.context().putWrap(SqlBeanMeta.class, beanWrap);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}