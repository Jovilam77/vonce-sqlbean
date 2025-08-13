package cn.vonce.sql.solon.config;

import cn.vonce.sql.java.annotation.DbTransactional;
import cn.vonce.sql.java.config.BaseAutoConfigMultiDataSource;
import cn.vonce.sql.solon.annotation.EnableAutoConfigMultiDataSource;
import cn.vonce.sql.solon.datasource.DynamicDataSource;
import cn.vonce.sql.solon.datasource.TransactionalInterceptor;
import cn.vonce.sql.uitls.StringUtil;
import org.noear.solon.core.*;

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
public class AutoConfigMultiDataSource extends BaseAutoConfigMultiDataSource implements BeanBuilder {

    private AppContext appContext;
    private Map<String, Object> propertyMap;

    @Override
    public Map<String, Object> getPropertyMap() {
        return this.propertyMap;
    }

    @Override
    public String getProperty(String key) {
        return appContext.cfg().get(key);
    }

    @Override
    public String getDataSourceType() {
        return "solon.datasource.type";
    }

    @Override
    public String getDataSourcePrefix() {
        return "solon.datasource.flexsql";
    }

    @Override
    public void doBuild(Class clz, BeanWrap bw, Annotation anno) throws Throwable {
        this.appContext = bw.context();
        EnableAutoConfigMultiDataSource enableAutoConfigMultiDataSource = (EnableAutoConfigMultiDataSource) anno;
        String defaultDataSource = null;
        Set<String> dataSourceNameSet = new LinkedHashSet();
        String dataSourcePrefix = this.getDataSourcePrefix();
        this.propertyMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : appContext.cfg().entrySet()) {
            if (entry.getKey() instanceof String) {
                String key = (String) entry.getKey();
                if (key.startsWith(dataSourcePrefix)) {
                    key = key.substring(key.indexOf(dataSourcePrefix) + dataSourcePrefix.length() + 1);
                    key = key.substring(0, key.indexOf("."));
                    dataSourceNameSet.add(key);
                    if (defaultDataSource == null) {
                        defaultDataSource = key;
                    }
                    this.propertyMap.put((String) entry.getKey(), entry.getValue());
                }
            }
        }
        String anonDefaultDataSource = enableAutoConfigMultiDataSource.defaultDataSource();
        if (anonDefaultDataSource != null && StringUtil.isNotBlank(anonDefaultDataSource)) {
            defaultDataSource = anonDefaultDataSource;
        }
        super.config(dataSourceNameSet, defaultDataSource, (defaultTargetDataSource, dataSourceMap) -> {
            DynamicDataSource dynamicDataSource = new DynamicDataSource();
            dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
            dynamicDataSource.setTargetDataSources(dataSourceMap);
            BeanWrap beanWrap = bw.context().wrap("dynamicDataSource", dynamicDataSource);
            bw.context().putWrap(DataSource.class, beanWrap);
            bw.context().beanInterceptorAdd(DbTransactional.class, new TransactionalInterceptor());
            AutoConfigSolon.init(beanWrap);
        });
    }
}
