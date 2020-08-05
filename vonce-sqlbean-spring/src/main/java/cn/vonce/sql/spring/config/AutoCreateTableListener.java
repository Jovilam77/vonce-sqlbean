package cn.vonce.sql.spring.config;

import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.spring.service.MybatisSqlBeanServiceImpl;
import cn.vonce.sql.spring.service.SpringJdbcSqlBeanServiceImpl;
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
            beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(MybatisSqlBeanServiceImpl.class)));
            beanNameList.addAll(Arrays.asList(evt.getApplicationContext().getBeanNamesForType(SpringJdbcSqlBeanServiceImpl.class)));
            if (!beanNameList.isEmpty()) {
                List<String> tableList = evt.getApplicationContext().getBean(beanNameList.get(0), MybatisSqlBeanServiceImpl.class).getTableService().getTableList();
                for (String name : beanNameList) {
                    MybatisSqlBeanServiceImpl mybatisSqlBeanService = evt.getApplicationContext().getBean(name, MybatisSqlBeanServiceImpl.class);
                    Class<?> clazz = mybatisSqlBeanService.getBeanClass();
                    if (clazz == null) {
                        continue;
                    }
                    SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
                    if (sqlTable != null && !sqlTable.isView() && sqlTable.autoCreate() && tableList != null && !tableList.isEmpty()) {
                        Table table = SqlBeanUtil.getTable(clazz);
                        if (tableList.contains(table.getName()) || tableList.contains(table.getName().toUpperCase()) || tableList.contains(table.getName().toLowerCase())) {
                            continue;
                        }
                        mybatisSqlBeanService.getTableService().createTable();
                        logger.info("-----'{}'表不存在，已为你自动创建-----", table.getName());
                    }
                }
            }

        }
    }

}
