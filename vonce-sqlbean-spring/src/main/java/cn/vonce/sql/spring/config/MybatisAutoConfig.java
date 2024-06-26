package cn.vonce.sql.spring.config;

import cn.vonce.sql.spring.mapper.MybatisSqlBeanMapperInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

import java.util.List;

/**
 * Mybatis自动配置
 *
 * @author Jovi
 * @version 1.0
 * @email imjovi@qq.com
 * @date 2019/8/21 13:05
 */
public class MybatisAutoConfig {

    private Logger logger = LoggerFactory.getLogger(MybatisAutoConfig.class);

    /**
     * Spring mvc模式使用，Spring boot为空
     */
    @Autowired(required = false)
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    @Conditional(ConditionalOnUseMybatis.class)
    public Interceptor interceptor() throws Exception {
        MybatisSqlBeanMapperInterceptor mybatisMapperInterceptor = new MybatisSqlBeanMapperInterceptor();
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactoryBean != null) {
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
        } else {
            try {
                sqlSessionFactory = beanFactory.getBean(SqlSessionFactory.class);
            } catch (Exception e) {
                logger.info("MybatisAutoConfig：{}", e.getMessage());
            }
        }
        if (sqlSessionFactory != null) {
            List<Interceptor> interceptorList = sqlSessionFactory.getConfiguration().getInterceptors();
            if (interceptorList != null) {
                if (!interceptorList.stream().anyMatch(item -> item instanceof MybatisSqlBeanMapperInterceptor)) {
                    sqlSessionFactory.getConfiguration().addInterceptor(mybatisMapperInterceptor);
                }
            }
        }
        return mybatisMapperInterceptor;
    }

}
