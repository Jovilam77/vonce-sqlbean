package cn.vonce.sql.spring.config;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SpringJdbc自动配置
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2025/06/09 10:51
 */
public class SpringJdbcAutoConfig {

    private Logger logger = LoggerFactory.getLogger(SpringJdbcAutoConfig.class);

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
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
