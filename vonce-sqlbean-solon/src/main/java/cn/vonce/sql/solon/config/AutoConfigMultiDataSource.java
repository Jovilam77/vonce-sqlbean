package cn.vonce.sql.solon.config;

import cn.vonce.sql.java.annotation.DbTransactional;
import cn.vonce.sql.java.config.BaseAutoConfigMultiDataSource;
import cn.vonce.sql.java.config.RegisterAutoConfigMultiDataSourceBean;
import cn.vonce.sql.solon.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.solon.datasource.DynamicDataSource;
import cn.vonce.sql.solon.datasource.TransactionalInterceptor;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.BeanInjector;
import org.noear.solon.core.BeanWrap;
import org.noear.solon.core.VarHolder;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 自动配置多数据源
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2021/7/7 17:00
 */
public class AutoConfigMultiDataSource extends BaseAutoConfigMultiDataSource implements BeanInjector {

    private AppContext appContext;

    @Override
    public String getProperty(String key) {
        return appContext.cfg().get(key);
    }

    @Override
    public void doInject(VarHolder vh, Annotation anno) {
        this.appContext = vh.context();
        EnableAutoConfigMultiDataSource enableAutoConfigMultiDataSource = (EnableAutoConfigMultiDataSource) anno;
        super.config(enableAutoConfigMultiDataSource.multiDataSource(), enableAutoConfigMultiDataSource.defaultDataSource(), new RegisterAutoConfigMultiDataSourceBean() {
            @Override
            public void registerBean(DataSource defaultTargetDataSource, Map<String, DataSource> dataSourceMap) {
                DynamicDataSource dynamicDataSource = new DynamicDataSource();
                dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
                dynamicDataSource.setTargetDataSources(dataSourceMap);
                vh.context().beanInject(dynamicDataSource);
                BeanWrap beanWrap = vh.context().wrap("dynamicDataSource", new DynamicDataSource());
                vh.context().putWrap("dynamicDataSource", beanWrap);
                vh.context().beanInterceptorAdd(DbTransactional.class, new TransactionalInterceptor());
            }
        });
    }

}
