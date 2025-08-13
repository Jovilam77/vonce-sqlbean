package cn.vonce.sql.spring.config;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * SpringJdbc自动配置
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2025/06/09 10:51
 */
public class SpringJdbcAutoConfig {

    private final java.util.logging.Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    @Bean(name = "sqlBeanMetaForSpringJdbc")
    public SqlBeanMeta sqlBeanMeta() {
        try {
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            SqlBeanMeta sqlBeanMeta = SqlBeanMeta.build(sqlBeanConfig, connection.getMetaData());
            connection.close();
            return sqlBeanMeta;
        } catch (SQLException e) {
            logger.warning(String.format("sqlBeanMeta：%s", e.getMessage()));
        }
        return null;
    }

}
