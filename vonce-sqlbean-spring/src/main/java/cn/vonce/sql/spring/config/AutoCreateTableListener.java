package cn.vonce.sql.spring.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.bean.TableInfo;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.service.TableService;
import cn.vonce.sql.uitls.SqlBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        //用户未进行配置或者配置了启用自动创建
        if ((evt.getApplicationContext().getParent() == null || evt.getApplicationContext().getParent().getParent() == null) && (sqlBeanConfig == null || sqlBeanConfig.getAutoCreate())) {
            new Thread(() -> {
                List<String> beanNameList = new ArrayList<>();
                beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(TableService.class)));
                if (!beanNameList.isEmpty()) {
                    Map<String, TableService> schemaMap = new HashMap<>();
                    for (String name : beanNameList) {
                        TableService tableService = evt.getApplicationContext().getBean(name, TableService.class);
                        Class<?> clazz = tableService.getBeanClass();
                        if (clazz == null) {
                            continue;
                        }
                        SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                        schemaMap.put(sqlTable.schema(), tableService);
                    }
                    for (Map.Entry<String, TableService> entry : schemaMap.entrySet()) {
                        List<TableInfo> tableList = entry.getValue().getTableList(null);
                        for (String name : beanNameList) {
                            TableService tableService = evt.getApplicationContext().getBean(name, TableService.class);
                            Class<?> clazz = tableService.getBeanClass();
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
                                            tableService.alterRemarks(sqlTable.remarks());
                                        }
                                        break;
                                    }
                                }
                                //创建表
                                if (!isExist && sqlTable.autoCreate()) {
                                    tableService.createTable();
                                    logger.info("-----'{}'表不存在，已为你自动创建-----", table.getName());
                                    continue;
                                }
                                //更新表结构
                                if (isExist && sqlTable.autoAlter()) {
                                    try {
                                        tableService.alter(table, tableService.getColumnInfoList(table.getName()));
                                    } catch (Exception e) {
                                        logger.error("更新表结构出错：" + e.getMessage(), e);
                                    }
                                }
                            }
                        }
                    }
                    schemaMap.clear();
                }
            }).start();
        }
    }

}
