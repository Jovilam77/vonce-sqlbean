package cn.vonce.sql.spring.service;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;

import java.util.Objects;

/**
 * SqlBeanServiceImpl 抽象类 获取配置通用实现
 *
 * @param
 * @author Jovi
 * @version 1.0
 * @email 766255988@qq.com
 */
public abstract class BaseSqlBeanServiceImpl {

    private SqlBeanDB sqlBeanDB;

    public abstract SqlBeanConfig getSqlBeanConfig();

    public abstract String getProductName();

    public SqlBeanDB getSqlBeanDB() {
        if (sqlBeanDB == null) {
            sqlBeanDB = new SqlBeanDB();
            sqlBeanDB.setSqlBeanConfig(getSqlBeanConfig());
            //如果用户未进行配置
            boolean isUserConfig = true;
            if (sqlBeanDB.getSqlBeanConfig() == null) {
                isUserConfig = false;
                sqlBeanDB.setSqlBeanConfig(new SqlBeanConfig());
            }
            DbType dbType = DbType.getDbType(getProductName());
            sqlBeanDB.setDbType(dbType);
            //如果用户未进行配置则对某些数据库进行设置
            if (!isUserConfig) {
                switch (Objects.requireNonNull(dbType)) {
                    case Oracle:
                    case DB2:
                    case Derby:
                    case Hsql:
                    case H2:
                        sqlBeanDB.getSqlBeanConfig().setToUpperCase(true);
                        break;
                }
            }
        }
        return sqlBeanDB;
    }

}
