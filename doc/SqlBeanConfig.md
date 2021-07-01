## 说明
```
如果未存在配置则会使用常规设置

```
## 配置
```java
@Configuration
public class XXXConfiguration {

    @Bean
    public SqlBeanConfig sqlBeanConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setToUpperCase(false);//是否转大写
        sqlBeanConfig.setAutoCreate(true);//是否表不存在时创建表，如果这里为false那么@SqlTable注解中autoCreate=true不会生效
        //sqlBeanConfig.setUniqueIdProcessor(xxx);//自主实现的id生成器
        return sqlBeanConfig;
    }
	
}
```