package cn.vonce.sql.solon.config;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.java.config.BaseAutoCreateTableListener;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Solon自动创建表监听类
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2024/8/20 12:10
 */
@Component
public class SolonAutoCreateTableListener extends BaseAutoCreateTableListener implements EventListener<AppLoadEndEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AppLoadEndEvent appLoadEndEvent;

    private final Lock lock = new ReentrantLock();

    @Override
    public void onEvent(AppLoadEndEvent appLoadEndEvent) {
        this.appLoadEndEvent = appLoadEndEvent;
        SqlBeanConfig sqlBeanConfig = appLoadEndEvent.context().getBeansOfType(SqlBeanConfig.class).stream().findFirst().orElse(null);
        if (sqlBeanConfig == null || sqlBeanConfig.getAutoCreate()) {
            lock.lock();
            try {
                new Thread(() -> processSqlBeanServices()).start();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T getBean(String name) {
        return appLoadEndEvent.context().getBean(name);
    }

    @Override
    public <T> List<T> getBeansForType(Class<T> baseType) {
        return appLoadEndEvent.context().getBeansOfType(baseType);
    }

    @Override
    public List<String> getBeanNamesForType(Class<?> baseType) {
        List<?> beanList = appLoadEndEvent.context().getBeansOfType(baseType);
        if (beanList != null) {
            List<String> beanNames = new ArrayList<>();
            for (Object bean : beanList) {
                beanNames.add(bean.getClass().getName());
            }
            return beanNames;
        }
        return null;
    }
}
