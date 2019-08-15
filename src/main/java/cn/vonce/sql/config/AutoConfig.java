package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.orm.mapper.MybatisSqlBeanMapper;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * 自动配置
 *
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 * @date 2019年6月25日下午12:7:50
 */
public class AutoConfig {

    @Bean
    @ConditionalOnUseMysql
    public SqlBeanConfig mysqlSqlBeanConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.MYSQL);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseOracle
    public SqlBeanConfig oracleSqlBeanConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.ORACLE);
        sqlBeanConfig.setOracleToUpperCase(true);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseSqlServer
    public SqlBeanConfig sqlServerSqlBeanConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.SQLSERVER2008);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @Conditional(ConditionalOnUseMybatis.class)
    public Interceptor interceptor() {
        MybatisSqlBeanMapper mybatisMapperInterceptor = new MybatisSqlBeanMapper();
        return mybatisMapperInterceptor;
    }

}
