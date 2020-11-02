package cn.vonce.sql.service;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDBConfig;
import cn.vonce.sql.enumerate.DbType;

import java.sql.SQLException;
import java.util.Objects;

public abstract class BaseSqlBeanServiceImpl {

    private SqlBeanDBConfig sqlBeanDBConfig;

    public abstract SqlBeanConfig getSqlBeanConfig();
    public abstract String getProductName();

    public SqlBeanDBConfig getSqlBeanDBConfig() {
        if (sqlBeanDBConfig == null) {
            sqlBeanDBConfig = new SqlBeanDBConfig();
            sqlBeanDBConfig.setSqlBeanConfig(getSqlBeanConfig());
            //如果用户未进行配置
            boolean isUserConfig = true;
            if (sqlBeanDBConfig.getSqlBeanConfig() == null) {
                isUserConfig = false;
                sqlBeanDBConfig.setSqlBeanConfig(new SqlBeanConfig());
            }
            DbType dbType = DbType.getDbType(getProductName());
            sqlBeanDBConfig.setDbType(dbType);
            //如果用户未进行配置则对某些数据库进行设置
            if (!isUserConfig) {
                switch (Objects.requireNonNull(dbType)) {
                    case Oracle:
                    case DB2:
                    case Derby:
                    case Hsql:
                    case H2:
                        sqlBeanDBConfig.getSqlBeanConfig().setToUpperCase(true);
                        break;
                }
            }
        }
        return sqlBeanDBConfig;
    }

}
