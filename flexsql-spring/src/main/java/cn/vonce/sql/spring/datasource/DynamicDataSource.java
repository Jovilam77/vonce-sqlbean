package cn.vonce.sql.spring.datasource;

import cn.vonce.sql.java.datasource.ConnectionContextHolder;
import cn.vonce.sql.java.datasource.ConnectionProxy;
import cn.vonce.sql.java.datasource.DataSourceContextHolder;
import cn.vonce.sql.java.datasource.TransactionalContextHolder;
import cn.vonce.sql.uitls.StringUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 动态数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/10/29 21:22
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        String xid = TransactionalContextHolder.getXid();
        String ds = DataSourceContextHolder.getDataSource();
        if (StringUtil.isEmpty(xid)) {
            return determineTargetDataSource().getConnection();
        }
        return getConnectionProxy(ds, null, null);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        String xid = TransactionalContextHolder.getXid();
        String ds = DataSourceContextHolder.getDataSource();
        if (StringUtil.isEmpty(xid)) {
            return determineTargetDataSource().getConnection(username, password);
        }
        return getConnectionProxy(ds, username, password);
    }

    private ConnectionProxy getConnectionProxy(String ds, String username, String password) throws SQLException {
        if (StringUtil.isBlank(ds)) {
            ds = "default";
        }
        ConnectionProxy connectionProxy = ConnectionContextHolder.getConnection(ds);
        if (connectionProxy == null) {
            Connection connection;
            if (StringUtil.isBlank(username) && StringUtil.isBlank(password)) {
                connection = determineTargetDataSource().getConnection();
            } else {
                connection = determineTargetDataSource().getConnection(username, password);
            }
            connectionProxy = new ConnectionProxy(ds, connection);
            ConnectionContextHolder.setConnection(ds, connectionProxy);
        }
        return connectionProxy;
    }

}
