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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自动创建表监听类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2020/8/3 10:51
 */
@Service
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
                    List<TableInfo> tableList = evt.getApplicationContext().getBean(beanNameList.get(0), TableService.class).getTableList(null);
                    if (tableList == null || tableList.size() == 0) {
                        return;
                    }
                    for (String name : beanNameList) {
                        TableService tableService = evt.getApplicationContext().getBean(name, TableService.class);
                        Class<?> clazz = tableService.getBeanClass();
                        if (clazz == null) {
                            continue;
                        }
                        SqlTable sqlTable = SqlBeanUtil.getSqlTable(clazz);
                        Table table = SqlBeanUtil.getTable(clazz);
                        //存在@SqlTable注解才会自动创建表和更新表结构
                        if (sqlTable != null && !sqlTable.isView()) {
                            boolean isExist = false;
                            //创建表
                            if (sqlTable.autoCreate()) {
                                for (TableInfo tableInfo : tableList) {
                                    if (tableInfo.getName().equalsIgnoreCase(table.getName())) {
                                        isExist = true;
                                        break;
                                    }
                                }
                                if (!isExist) {
                                    tableService.createTable();
                                    logger.info("-----'{}'表不存在，已为你自动创建-----", table.getName());
                                    continue;
                                }
                            }
                            //更新表结构
                            if (sqlTable.autoAlter()) {
                                tableService.alter(table, tableService.getColumnInfoList(table.getName()));
                            }
                        }
                    }
                }
            }).start();
        }
    }

}
