## 说明
```
大部分的Spring MVC和Spring Boot项目在项目启动时则会进行自动配置，不排除有某些项目存在自动配置失败的情况，
目前暂时不支持多数据源项目自动配置，如自动配置失败请参考以下方式手动配置。

```
## 配置
```java
@Configuration
public class XXXConfiguration {

    @Bean
    public SqlBeanConfig sqlBeanConfig() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.MySQL);//数据库类型
        sqlBeanConfig.setToUpperCase(false);//是否转大写
        sqlBeanConfig.setAutoCreate(true);//是否表不存在时创建表，如果这里为false那么@SqlTable注解中autoCreate=true不会生效
		return sqlBeanConfig;
    }
	
}
```