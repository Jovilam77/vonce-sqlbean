package cn.vonce.sql.config;

import cn.vonce.sql.orm.mapper.MybatisSqlBeanMapper;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
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
public class MybatisAutoConfig extends AutoConfig {

    @Autowired(required = false)
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Bean
    @Conditional(ConditionalOnUseMybatis.class)
    public Interceptor interceptor() throws Exception {
        MybatisSqlBeanMapper mybatisMapperInterceptor = new MybatisSqlBeanMapper();
        if (sqlSessionFactoryBean != null) {
            List<Interceptor> interceptorList = sqlSessionFactoryBean.getObject().getConfiguration().getInterceptors();
            if (interceptorList != null) {
                boolean exist = false;
                for (Interceptor interceptor : interceptorList) {
                    if (interceptor instanceof MybatisSqlBeanMapper) {
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    sqlSessionFactoryBean.getObject().getConfiguration().addInterceptor(mybatisMapperInterceptor);
                }
            }
        }
        return mybatisMapperInterceptor;
    }

}
