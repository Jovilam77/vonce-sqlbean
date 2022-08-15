package cn.vonce.sql.spring.service;

import cn.vonce.sql.bean.ColumnInfo;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.uitls.StringUtil;

import java.util.List;
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

    /**
     * 处理字段信息
     *
     * @param columnInfoList
     */
    public void handleColumnInfo(List<ColumnInfo> columnInfoList) {
        DbType dbType = getSqlBeanDB().getDbType();
        if (dbType == DbType.Derby) {
            for (ColumnInfo info : columnInfoList) {
                String[] values = info.getName().split(" ");
                //如果存在空格分割说明字段名后面存在NOT NULL，即不能为空
                if (values.length > 1) {
                    info.setNotnull(true);
                } else {
                    info.setNotnull(false);
                }
                //设置字段名
                info.setName(StringUtil.getWord(values[0]));
                //设置字段长度范围
                String range[] = StringUtil.getBracketContent(values[0]).split(",");
                info.setLength(Integer.parseInt(range[0]));
                info.setScale(range.length == 1 ? 0 : Integer.parseInt(range[1]));
            }
        } else if (dbType == DbType.H2) {
            for (ColumnInfo info : columnInfoList) {
                if ("CHARACTER VARYING".equalsIgnoreCase(info.getName())) {
                    info.setType("varchar");
                } else if ("CHARACTER LARGE OBJECT".equalsIgnoreCase(info.getName())) {
                    info.setType("longtext");
                } else if ("BINARY VARYING".equalsIgnoreCase(info.getName())) {
                    info.setType("blob");
                } else if ("BINARY LARGE OBJECT".equalsIgnoreCase(info.getName())) {
                    info.setType("longblob");
                }
            }
        }
    }

}
