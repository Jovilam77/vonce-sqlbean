package cn.vonce.sql.spring.config;

import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.java.config.BaseAutoCreateTableListener;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
public class AutoCreateTableListener extends BaseAutoCreateTableListener implements ApplicationListener<ContextRefreshedEvent> {

    private ContextRefreshedEvent contextRefreshedEvent;

    private final Lock lock = new ReentrantLock();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent evt) {
        this.contextRefreshedEvent = evt;
        SqlBeanConfig sqlBeanConfig = evt.getApplicationContext().getBeansOfType(SqlBeanConfig.class).values().stream().findFirst().orElse(null);
        if ((evt.getApplicationContext().getParent() == null || evt.getApplicationContext().getParent().getParent() == null) && (sqlBeanConfig == null || sqlBeanConfig.getAutoCreate())) {
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
        return (T) this.contextRefreshedEvent.getApplicationContext().getBean(name);
    }

    @Override
    public <T> List<T> getBeansForType(Class<T> baseType) {
        Map<String, T> beanMap = this.contextRefreshedEvent.getApplicationContext().getBeansOfType(baseType);
        if (beanMap != null) {
            return new ArrayList<>(beanMap.values());
        }
        return null;
    }

    @Override
    public List<String> getBeanNamesForType(Class<?> baseType) {
        return Arrays.asList(this.contextRefreshedEvent.getApplicationContext().getBeanNamesForType(baseType));
    }

}
