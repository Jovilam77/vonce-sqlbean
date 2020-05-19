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
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseMariaDB
    public SqlBeanConfig mariaDBConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.MariaDB);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseOracle
    public SqlBeanConfig oracleConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.Oracle);
        sqlBeanConfig.setToUpperCase(true);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseSqlServer
    public SqlBeanConfig sqlServerConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.SQLServer2008);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUsePostgreSql
    public SqlBeanConfig postgreSqlConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.PostgreSQL);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseDB2
    public SqlBeanConfig db2Config() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.DB2);
        sqlBeanConfig.setToUpperCase(true);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseDerby
    public SqlBeanConfig derbyConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.Derby);
        sqlBeanConfig.setToUpperCase(true);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseSqlite
    public SqlBeanConfig sqliteConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.SQLite);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseHSql
    public SqlBeanConfig hsqlConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.Hsql);
        sqlBeanConfig.setToUpperCase(true);
        return sqlBeanConfig;
    }

    @Bean
    @ConditionalOnUseH2
    public SqlBeanConfig h2Config() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig(DbType.H2);
        sqlBeanConfig.setToUpperCase(true);
        return sqlBeanConfig;
    }

}
