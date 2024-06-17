package cn.vonce.sql.spring.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.bean.TableInfo;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.service.DbManageService;
import cn.vonce.sql.service.SqlBeanService;
import cn.vonce.sql.service.AdvancedDbManageService;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自动创建表监听类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/8/3 10:51
 */
@Component
public class AutoCreateTableListener implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private SqlBeanConfig sqlBeanConfig;

    private final Lock lock = new ReentrantLock();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        if ((evt.getApplicationContext().getParent() == null || evt.getApplicationContext().getParent().getParent() == null) && (sqlBeanConfig == null || sqlBeanConfig.getAutoCreate())) {
            lock.lock();
            try {
                new Thread(() -> processSqlBeanServices(evt)).start();
            } finally {
                lock.unlock();
            }
        }
    }

    private void processSqlBeanServices(ContextRefreshedEvent evt) {
        List<String> beanNameList = new ArrayList<>();
        beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(SqlBeanService.class)));
        if (!beanNameList.isEmpty()) {
            Map<String, SqlBeanService> schemaMap = new HashMap<>();
            for (String name : beanNameList) {
                SqlBeanService sqlBeanService = evt.getApplicationContext().getBean(name, SqlBeanService.class);
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
                    if (entry.getValue().getSqlBeanDB().getDbType() == DbType.SQLite || entry.getValue().getSqlBeanDB().getDbType() == DbType.Oracle) {
                        continue;
                    }
                    List<String> databases = ((AdvancedDbManageService) entry.getValue()).getSchemas(entry.getKey());
                    if (databases == null || databases.isEmpty()) {
                        ((AdvancedDbManageService) entry.getValue()).createSchema(entry.getKey());
                        logger.info("-----Schema:[{}]不存在,已为你自动创建-----", entry.getKey());
                    }
                }
                List<TableInfo> tableList = ((DbManageService) entry.getValue()).getTableList();
                for (String name : beanNameList) {
                    SqlBeanService sqlBeanService = evt.getApplicationContext().getBean(name, SqlBeanService.class);
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
                                //表注释不一致
                                if (sqlTable.autoAlter() && !sqlTable.remarks().equals(tableInfo.getRemarks())) {
                                    ((AdvancedDbManageService) sqlBeanService).alterRemarks(sqlTable.remarks());
                                }
                                break;
                            }
                        }
                        //创建表
                        if (!isExist && sqlTable.autoCreate()) {
                            ((AdvancedDbManageService) sqlBeanService).createTable();
                            logger.info("-----Table:[{}]不存在,已为你自动创建-----", (StringUtil.isNotEmpty(table.getSchema()) ? table.getSchema() + "." + table.getName() : table.getName()));
                            continue;
                        }
                        //更新表结构
                        if (isExist && sqlTable.autoAlter()) {
                            try {
                                ((AdvancedDbManageService) sqlBeanService).alter(table, ((AdvancedDbManageService) sqlBeanService).getColumnInfoList(table.getName()));
                            } catch (Exception e) {
                                logger.error("更新表结构出错：" + e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            schemaMap.clear();
        }
    }

}
