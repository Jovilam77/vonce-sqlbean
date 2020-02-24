## Sqlbean(Sql生成助手)
#### 介绍
###### 这是一款使用Java面向对象思想来编写并生成Sql语句的助手，并在此基础上对Mybatis、Spring Jdbc实现了轻量级的插件支持。该插件的目的是为了提高开发效率，减少大量的Sql语句编写，让开发者更专注于业务代码的编写。

###### 特点：零配置，支持联表查询，支持Mybatis、Spring Jdbc，支持分页
###### 环境：JDK7+，Mybatis3.2.4+，(Spring MVC 4.1.2+ 或 Spring Boot 1x 或 Spring Boot 2x)
###### 数据库：Mysql，MariaDB，Oracle，Sqlserver2008+，PostgreSQL，DB2，Derby，Sqlite，HSQL，H2

#### 简单上手
###### 1：引入Maven依赖
    <dependency>
    	<groupId>cn.vonce</groupId>
    	<artifactId>vonce-sqlbean</artifactId>
    	<version>1.2.5</version>
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
###### 3：无需Dao层，Service层接口无需写任何方法（如果想写原始Sql可以自己定义）只需继承SqlBeanService<实体类>

```java
public interface EssayService extends SqlBeanService<Essay> {

}
```
###### 4：Service实现类无需实现任何方法（如果想写原始Sql可以自己实现）只需继承MybatisSqlBeanServiceImpl<实体类>和实现你的Service接口
```java
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay> implements EssayService {

}
```
###### 5：Controller层
```java
@Autowired
private EssayService essayService;

//查询
@GetMapping("get")
public RS get() {
    List<Essay> list = essayService.selectAll();
    list = essayService.selectByCondition("type = ?" , 2);
    Essay essay = essayService.selectById(1);
    essay = essayService.selectOneByCondition("id = ?" , 1);
    return super.successHint("获取成功", list);
    // 更多用法请查看下方详细文档...
}

//查询
@GetMapping("getList")
public RS getList(HttpServletRequest request) {
    // 查询对象
    //Select select = new Select();
    // 分页助手
    //PageHelper<Essay> pageHelper = new PageHelper<>(request);
    // 分页查询
    //pageHelper.paging(select, essayService);
    // 返回结果
    //return super.customHint(pageHelper.toResult("获取列表成功"));
    // 或者这样
    return super.customHint(new PageHelper<Essay>(request).paging(new Select(),essayService).toResult("获取文章列表成功"));
    // 更多用法请查看下方详细文档...
}

//更新
@PostMapping("update")
public RS update(Essay essay) {
    long i = essayService.updateByBeanId(essay, true);
    if (i > 0) {
        return super.successHint("更新成功");
    }
    return super.othersHint("更新失败");
    // 更多用法请查看下方详细文档...
}

//删除
@PostMapping("deleteById")
public RS deleteById(Integer[] id) {
    long i = essayService.deleteById(id);
    if (i > 0) {
        return super.successHint("删除成功");
    }
    return super.othersHint("删除失败");
    // 更多用法请查看下方详细文档...
}

//插入
@PostMapping("add")
public RS add() {
    List<Essay> essayList = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        Essay essay = new Essay(i, "name" + i);
        essayList.add(essay);
    }
    essayService.insert(essayList);
    return successHint("成功");
    // 更多用法请查看下方详细文档...
}
```

###### 以上即可实现无条件分页查询
###### 如果使用的是Spring JDBC那么将“MybatisSqlBeanServiceImpl”改为“SpringJdbcSqlBeanServiceImpl”即可
[========]

##### ↓更多用法请查看下方文档↓

#### [Select](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Select.md "Select")
#### [Insert](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Insert.md "Insert")
#### [Delete](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Delete.md "Delete")
#### [Update](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Update.md "Update")
#### [Service接口和实现类](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Interface.md "Service接口和实现类")
#### [注解与联表查询](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Annotation.md "注解与联表查询")
#### [分页查询](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Paging.md "分页查询")
#### [条件操作](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/SqlOperator.md "条件操作")
#### [条件逻辑](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/SqlLogic.md "条件逻辑")
#### [BaseController](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/BaseController.md "BaseController")