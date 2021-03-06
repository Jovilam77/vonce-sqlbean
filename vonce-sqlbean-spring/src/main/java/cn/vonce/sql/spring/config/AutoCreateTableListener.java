package cn.vonce.sql.spring.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
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
        if (evt.getApplicationContext().getParent() == null && (sqlBeanConfig == null || sqlBeanConfig.getAutoCreate())) {
            List<String> beanNameList = new ArrayList<>();
            beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(TableService.class)));
            if (!beanNameList.isEmpty()) {
                List<String> tableList = evt.getApplicationContext().getBean(beanNameList.get(0), TableService.class).getTableList();
                for (String name : beanNameList) {
                    TableService tableService = evt.getApplicationContext().getBean(name, TableService.class);
                    Class<?> clazz = tableService.getBeanClass();
                    if (clazz == null) {
                        continue;
                    }
                    SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
                    if (sqlTable != null && !sqlTable.isView() && sqlTable.autoCreate()) {
                        Table table = SqlBeanUtil.getTable(clazz);
                        if (tableList != null && !tableList.isEmpty()) {
                            if (tableList.contains(table.getName()) || tableList.contains(table.getName().toUpperCase()) || tableList.contains(table.getName().toLowerCase())) {
                                continue;
                            }
                        }
                        tableService.createTable();
                        logger.info("-----'{}'表不存在，已为你自动创建-----", table.getName());
                    }
                }
            }

        }
    }

}
