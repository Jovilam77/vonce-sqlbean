package cn.vonce.sql.spring.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.service.SqlBeanService;
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

    @Autowired
    private SqlBeanConfig sqlBeanConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        if (evt.getApplicationContext().getParent() == null && sqlBeanConfig.getAutoCreate()) {
            List<String> beanNameList = new ArrayList<>();
            beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(SqlBeanService.class)));
            if (!beanNameList.isEmpty()) {
                List<String> tableList = evt.getApplicationContext().getBean(beanNameList.get(0), SqlBeanService.class).getTableService().getTableList();
                for (String name : beanNameList) {
                    SqlBeanService sqlBeanService = evt.getApplicationContext().getBean(name, SqlBeanService.class);
                    Class<?> clazz = sqlBeanService.getBeanClass();
                    if (clazz == null) {
                        continue;
                    }
                    SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
                    if (sqlTable != null && !sqlTable.isView() && sqlTable.autoCreate() && tableList != null && !tableList.isEmpty()) {
                        Table table = SqlBeanUtil.getTable(clazz);
                        if (tableList.contains(table.getName()) || tableList.contains(table.getName().toUpperCase()) || tableList.contains(table.getName().toLowerCase())) {
                            continue;
                        }
                        sqlBeanService.getTableService().createTable();
                        logger.info("-----'{}'表不存在，已为你自动创建-----", table.getName());
                    }
                }
            }

        }
    }

}
