package cn.vonce.sql.java.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.bean.TableInfo;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.service.AdvancedDbManageService;
import cn.vonce.sql.service.DbManageService;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

import java.util.*;
import java.util.logging.Logger;

/**
 * 自动创建表监听 基础类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/8/3 10:51
 */
public abstract class BaseAutoCreateTableListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public abstract <T> T getBean(String name);

    public abstract <T> List<T> getBeansForType(Class<T> baseType);

    public abstract List<String> getBeanNamesForType(Class<?> baseType);

    public void processSqlBeanServices() {
        List<SqlBeanService> beanServiceList = this.getBeansForType(SqlBeanService.class);
        //去除重复的
        Map<Class<?>, SqlBeanService> beanServiceMap = null;
        if (beanServiceList != null && !beanServiceList.isEmpty()) {
            beanServiceMap = new HashMap<>();
            for (SqlBeanService sqlBeanService : beanServiceList) {
                beanServiceMap.put(sqlBeanService.getBeanClass(), sqlBeanService);
            }
        }
        if (beanServiceMap != null) {
            Map<String, SqlBeanService> schemaMap = new HashMap<>();
            for (SqlBeanService sqlBeanService : beanServiceMap.values()) {
                Class<?> clazz = sqlBeanService.getBeanClass();
                if (clazz == null) {
                    continue;
                }
                SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                schemaMap.put(sqlTable.schema(), sqlBeanService);
            }
            for (Map.Entry<String, SqlBeanService> entry : schemaMap.entrySet()) {
                //检查schema是否存在,不存在则创建（不支持sqlite和oracle）
                if (StringUtil.isNotBlank(entry.getKey())) {
                    if (entry.getValue().getSqlBeanMeta().getDbType() == DbType.SQLite || entry.getValue().getSqlBeanMeta().getDbType() == DbType.Oracle) {
                        continue;
                    }
                    List<String> databases = ((AdvancedDbManageService) entry.getValue()).getSchemas(entry.getKey());
                    if (databases == null || databases.isEmpty()) {
                        ((AdvancedDbManageService) entry.getValue()).createSchema(entry.getKey());
                        logger.info(String.format("-----Schema:[%s]不存在,已为你自动创建-----", entry.getKey()));
                    }
                }
                List<TableInfo> tableList = ((DbManageService) entry.getValue()).getTableList();
                for (SqlBeanService sqlBeanService : beanServiceMap.values()) {
                    Class<?> clazz = sqlBeanService.getBeanClass();
                    if (clazz == null) {
                        continue;
                    }
                    SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                    Table table = SqlBeanUtil.getTable(clazz);
                    //存在@SqlTable注解且不是视图且schema一致的才会自动创建表和更新表结构
                    if (sqlTable != null && !sqlTable.isView() && sqlTable.schema().equals(entry.getKey())) {
                        boolean isExist = false;
                        //检查表是否存在
                        for (TableInfo tableInfo : tableList) {
                            if (tableInfo.getName().equalsIgnoreCase(table.getName())) {
                                isExist = true;
                                String remarks = sqlTable.remarks();
                                //如果没有设置表注释，则从类上获取
                                if (StringUtil.isEmpty(remarks)) {
                                    remarks = SqlBeanUtil.getBeanRemarks(SqlBeanUtil.getConstantClass(clazz));
                                }
                                //表注释不一致
                                if (sqlTable.autoAlter() && !remarks.equals(tableInfo.getRemarks())) {
                                    ((AdvancedDbManageService) sqlBeanService).alterRemarks(remarks);
                                }
                                break;
                            }
                        }
                        //创建表
                        if (!isExist && sqlTable.autoCreate()) {
                            try {
                                ((AdvancedDbManageService) sqlBeanService).createTable();
                                logger.info(String.format("-----Table:[%s]不存在,已为你自动创建-----", (StringUtil.isNotEmpty(table.getSchema()) ? table.getSchema() + "." + table.getName() : table.getName())));
                                continue;
                            } catch (Exception e) {
                                logger.warning(String.format("创建表结构出错：" + e.getMessage()));
                            }
                        }
                        //更新表结构
                        if (isExist && sqlTable.autoAlter()) {
                            try {
                                ((AdvancedDbManageService) sqlBeanService).alter(table, ((AdvancedDbManageService) sqlBeanService).getColumnInfoList(table.getName()));
                            } catch (Exception e) {
                                logger.warning(String.format("更新表结构出错：" + e.getMessage()));
                            }
                        }
                    }
                }
            }
            schemaMap.clear();
        }
    }

}
