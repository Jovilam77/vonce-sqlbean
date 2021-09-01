## 多数据源、动态Schema、读写分离、SQL生成配置文档
#### 一、多数据源使用
###### 1. 数据源相关属性yml格式（注解自动配置必须准守，手动配置可自定义）
```
#使用注解配置必须遵守spring.datasource.type否则找不到或不配将默认使用druid，如未导包会报错
#使用注解配置必须遵守spring.datasource.sqlbean前缀
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    sqlbean:
      sqlite:
        driverClassName: org.sqlite.JDBC
        url: jdbc:sqlite:config.db
        username: 
        password: 
        initialSize: 1
        minIdle: 3
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 30000
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
      mysqlread:
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/read?useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password: 123456
        initialSize: 1
        minIdle: 3
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 30000
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
      mysqlwrite:
        driverClassName: com.mysql.jdbc.Driver
        url: jdbc:mysql://127.0.0.1:3306/write?useSSL=false&serverTimezone=Asia/Shanghai
        username: root
        password: 123456
        initialSize: 1
        minIdle: 3
        maxActive: 20
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 30000
        validationQuery: select 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false

```
###### 2. 新建多数据源名称常量类，值必须与yml中一致（注解自动配置必须准守，手动配置可自定义）
```java
public class MultiDataSource {

    public static final String SQLITE = "sqlite";
	public static final String MYSQL_READ = "mysqlread";
	public static final String MYSQL_WRITE = "mysqlwrite";
	
}
```
###### 3. 使用注解自动配置@EnableAutoConfigMultiDataSource
```java
//defaultDataSource如果不设置将使用常量第一个值作为默认数据源
@EnableAutoConfigMultiDataSource(multiDataSource = MultiDataSource.class, defaultDataSource = MultiDataSource.MYSQL_READ)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
###### 4. 手动配置（使用步骤3忽略此步）
```java
@Configuration
public class DataSourceConfiguration {

    @Bean(name = "sqliteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlbean.sqlite")
    public DataSource getDateSourceSqlite() {
        return DruidDataSourceBuilder.create().build();
    }


    @Bean(name = "mysqlReadDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlbean.mysqlread")
    public DataSource getDateSourceMysqlRead() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlWriteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sqlbean.mysqlwrite")
    public DataSource getDateSourceMysqlWrite() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
		Map<Object, Object> dataSourceMap = new HashMap<>(3);
		dataSourceMap.put(DataSourceName.SQLITE, getDateSourceSqlite());
		dataSourceMap.put(DataSourceName.MYSQL_READ, getDateSourceMysqlRead());
		dataSourceMap.put(DataSourceName.MYSQL_WRITE, getDateSourceMysqlWrite());
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setDefaultTargetDataSource(getDateSourceMysqlRead());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

}
```
###### 5. 指定数据源和读写分离
```java
/**
 * 1. 写(主)库只能有一个，读(从)库可以有多个，master和slave都配置则用于读写分离。
 * 2. master和slave同时配置之后，Service内置方法将自动启用读写分离，
 *    如Service中自定义SQL查询方法需自行在方法中指定读或写，使用@DbSwitch主键，如不指定默认读写都走主库。
 * 3. 如果不需要读写分离，Service仅需指定某个数据源，那么只要配置master即可。
 * 4. 如果不使用@DbSource注解，那么该Service将使用默认数据源
 * 
 */
@DbSource(master = MultiDataSource.MYSQL_WRITE, slave = {MultiDataSource.MYSQL_READ})
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay,Long> implements EssayService {
	
	/**
	 * 这是自定义的查询方法
	 */
	@DbSwitch(DbRole.SLAVE)
	@Override
	public Essay getById(Long id) {
		//这里可能是你自己定的dao查询
		return super.selectById(id);
	}
	
	/**
	 * 这是自定义的保存方法
	 */
	@DbSwitch(DbRole.MASTER)
	@Override
	public Essay save(Essay essay) {
		//这里可能是你自己定的dao保存
		return super.insert(essay);
	}
	
}
```
#### 二、动态Schema使用
###### 1. 配置获取schema
```java
@Component
@Aspect
public class SchemaAspect extends AbstractDynSchemaAspect {

    //仅供参考，具体根据自身业务来获取schema
    @Override
    public String getSchema() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			String schema = requestAttributes.getAttribute("schema", RequestAttributes.SCOPE_SESSION);
			return schema;
		}
		return null;
	}
	
}
```
###### 2. 业务实现类使用@DbDynSchema即可
```java
@DbDynSchema
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay,Long> implements EssayService {
	
}
```
#### 三、SqlBeanConfig配置
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
