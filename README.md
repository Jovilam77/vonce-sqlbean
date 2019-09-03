## Sqlbean(Sql生成助手)
#### 介绍
###### 这是一款使用Java面向对象思想来编写并生成Sql语句的助手，并在此基础上对Mybatis、Spring Jdbc实现了轻量级的插件支持。该插件的目的是为了提高开发效率，减少大量的Sql语句编写，让开发者更专注于业务代码的编写。

###### 特点：零配置，支持联表查询，支持Mybatis、Spring Jdbc，支持分页
###### 环境：JDK7+，Mybatis3.2.4+，(Spring MVC 4.1.2+ 或 Spring Boot 1x 或 Spring Boot 2x)
###### 数据库：Mysql，MariaDB，Oracle，Sqlserver2008+，PostgreSQL，DB2

#### 简单上手
###### 1：引入Maven依赖
    <dependency>
    	<groupId>cn.vonce</groupId>
    	<artifactId>vonce-sqlbean</artifactId>
    	<version>1.0.0</version>
    </dependency>
###### 2：标注实体类，也可以不写，但类名与字段名必须与数据库表名字段名保持一致
```java
@SqlBeanTable("d_essay")
public class Essay {

    @SqlBeanField(value = "id" , id = true)
    private String id;

    @SqlBeanField(value = "userId" )
    private String userId;

    @SqlBeanField(value = "content" )
    private String content;

    @SqlBeanField(value = "creationTime" )
    private Date creationTime;
	
    /**省略get set方法*/
	
}
```
###### 3：无需Dao层，Service层接口无需写任何方法（如果想写原始Sql可以自己定义）只需继承SqlBeanService<T>
```java
public interface EssayService<T> extends SqlBeanService<T> {

}
```
###### 4：Service实现类无需实现任何方法（如果想写原始Sql可以自己实现）只需继承SqlBeanServiceImpl<T>和实现你的Service接口
```java
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay> implements EssayService<Essay> {

}
```
###### 5：Controller层
```java
@Autowired
private EssayService<Essay> essayService;

@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public RS getList(HttpServletRequest request, HttpServletResponse response) {
      // 查询对象
      Select select = new Select();
      // 分页助手
      PageHelper<Essay> pageHelper = new PageHelper<>(request);
      // 分页查询
      pageHelper.paging(select, essayService);
      // 返回结果
      return super.customHint(pageHelper.toResult("获取文章列表成功"));
}
```
###### 或者这样
```java
return super.customHint(new PageHelper<Essay>(request).paging(new Select(),essayService).toResult("获取文章列表成功"));
```
###### 以上即可实现无条件分页查询
###### 如果使用的是Spring JDBC那么将“MybatisSqlBeanServiceImpl”改为“SpringJdbcSqlBeanServiceImpl”即可

#### [Select][1]
[1]:  "Select"
#### [Insert][1]
[1]:  "Insert"
#### [Delete][1]
[1]:  "Delete"
#### [Update][1]
[1]:  "Update"
#### [Service接口和实现类][355]
[355]:  "Service接口和实现类"
#### [注解与联表查询][355]
[355]:  "注解与联表查询"
#### [分页查询][355]
[355]:  "分页查询"
#### [条件操作][355]
[355]:  "条件操作"
#### [条件逻辑][355]
[355]:  "条件逻辑"
#### [BaseController][355]
[355]:  "BaseController"