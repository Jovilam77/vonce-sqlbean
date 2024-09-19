package cn.vonce.sql.solon.config;

import cn.vonce.sql.java.mapper.MybatisSqlBeanMapperInterceptor;
import org.apache.ibatis.solon.MybatisAdapter;
import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

import javax.sql.DataSource;

/**
 * Solon Mybatis 配置
 *
 * @author Jovi
 * @email imjovi@qq.com
 * @date 2024/8/12 15:27
 */
public class AutoConfigSolon implements Plugin {
    @Override
    public void start(AppContext context) {
        context.subWrapsOfType(DataSource.class, (bw) -> {
            MybatisAdapter mybatisAdapter = MybatisAdapterManager.get(bw);
            if (mybatisAdapter != null) {
                mybatisAdapter.getConfiguration().addMapper(cn.vonce.sql.java.dao.MybatisSqlBeanDao.class);
                mybatisAdapter.getConfiguration().addInterceptor(new MybatisSqlBeanMapperInterceptor());
            }
            context.beanMake(SolonAutoCreateTableListener.class);
        });

    }
}