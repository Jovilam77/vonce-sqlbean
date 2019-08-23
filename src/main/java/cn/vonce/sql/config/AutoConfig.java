package cn.vonce.sql.config;

import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.helper.SqlHelper;
import org.springframework.context.annotation.Bean;

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
    public SqlBeanConfig mysqlConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.MySQL);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseMariaDB
    public SqlBeanConfig mariaDBConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.MariaDB);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseOracle
    public SqlBeanConfig oracleConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.Oracle);
        sqlBeanConfig.setToUpperCase(true);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseSqlServer
    public SqlBeanConfig sqlServerConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.SQLServer2008);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUsePostgreSql
    public SqlBeanConfig postgreSqlConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.PostgreSQL);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseDB2
    public SqlBeanConfig db2Config() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.DB2);
        sqlBeanConfig.setToUpperCase(true);
        SqlHelper.init(sqlBeanConfig);
        return sqlBeanConfig;
    }

}
