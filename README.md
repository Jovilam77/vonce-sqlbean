## Sqlbean(Sql生成助手)
#### 介绍
###### 这是一款使用Java面向对象思想来编写并生成Sql语句的工具，并在此基础上同时对Mybatis和Spring Jdbc实现了类似于JPA的轻量级插件支持。插件中内置大量常用Sql执行的方法，目的是为了提高开发效率，减少大量的Sql语句编写，让开发者更专注于业务代码的编写。

###### 特点：零配置，自动建表，连表查询，乐观锁，分页，支持Mybatis、Spring Jdbc
###### 环境：JDK7+，Mybatis3.2.4+，(Spring MVC 4.1.2+ 或 Spring Boot 1x 或 Spring Boot 2x)
###### 数据库：Mysql，MariaDB，Oracle，Sqlserver2008+，PostgreSQL，DB2，Derby，Sqlite，HSQL，H2

###### Sqlbean For Android请移步这里👉 [gitee](https://gitee.com/iJovi/vonce-sqlbean-android "vonce-sqlbean-android")， [github](https://github.com/Jovilam77/vonce-sqlbean-android "vonce-sqlbean-android")


#### 简单上手
###### 1：引入Maven依赖
	<dependency>
		<groupId>cn.vonce</groupId>
		<artifactId>vonce-sqlbean-spring</artifactId>
		<version>1.4.1</version>
	</dependency>
###### 2：标注实体类
```java
@SqlTable("d_essay")
public class Essay {

	@SqlId(generateType = GenerateType.UUID)
	@SqlColumn("id")
	private String id;

	@SqlColumn("userId" )
	private String userId;

	@SqlColumn("content" )
	private String content;

	@SqlColumn("creationTime" )
	private Date creationTime;
	
	/**省略get set方法*/
	
}
```
###### 3：无需Dao层，Service层接口只需继承SqlBeanService<实体类,ID>

```java
public interface EssayService extends SqlBeanService<Essay,String> {

	//已内置大量常用查询、更新、删除、插入方法，这里可以写自己封装的方法

	Essay selectByUserId(String userId);
	
	List<Essay> selectByTimeRange(String start,String end);
	
	long updateEssayById(String userId);

}
```
###### 4：Service实现类只需继承MybatisSqlBeanServiceImpl<实体类,ID>和实现你的Service接口
```java
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay,String> implements EssayService {

	public Essay selectByUserId(String userId){
		return super.selectOneByCondition("& = ?", SqlEssay.userId, userId);
	}
	
	public List<Essay> selectByTimeRange(String start,String end){
		return super.selectByCondition("& > ? AND & < ?", SqlEssay.creationTime, start, SqlEssay.creationTime, end);
	}
	
	public long updateEssayById(String userId){
		Essay essay = new Essay();
		essay.setId(userId);
		essay.setContent("欢迎使用Sqlbean");
		//return super.updateByCondition(essay, true, "& = ?", SqlEssay.id, id);
		//return updateById(essay, userId, true);
		return updateByBeanId(essay, true);
	}
	
	//更多用法查阅详细文档

}
```
###### 5：Controller层
```java
@RequestMapping("essay")
@RestController
public class EssayController {
	
	@Autowired
	private EssayService essayService;

	//查询
	@GetMapping("get")
	public RS get() {
		List<Essay> list = essayService.selectAll();
		list = essayService.selectByCondition("& = ?" , SqlEssay.type, 2);
		Essay essay = essayService.selectById(1);
		essay = essayService.selectOneByCondition("& = ?" , SqlEssay.id, 1);
		return super.successHint("获取成功", list);
		// 更多用法请查看下方详细文档...
	}

	//分页
	@GetMapping("getList")
	public RS getList(HttpServletRequest request) {
		// 查询对象
	//Select select = new Select();
	// 分页助手PageHelper
	//ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);
	// 分页查询
	//pageHelper.paging(select, essayService);
	// 返回结果
	//return super.customHint(pageHelper.toResult("获取列表成功"));
	// 或者这样
	return super.customHint(new PageHelper<Essay>(request).paging(new Select(),essayService).toResult("获取文章列表成功"));
	}

	//更新
	@PostMapping("update")
	public RS update(String id) {
		long i = essayService.updateEssayById(id);
		if (i > 0) {
			return super.successHint("更新成功");
		}
		return super.othersHint("更新失败");
	}

	//删除
	@PostMapping("deleteById")
	public RS deleteById(Integer[] id) {
		long i = essayService.deleteById(id);
		if (i > 0) {
			return super.successHint("删除成功");
		}
		return super.othersHint("删除失败");
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
	}

}
// 更多用法请查看下方详细文档...
```
###### 如果使用的是Spring JDBC那么将“MybatisSqlBeanServiceImpl”改为“SpringJdbcSqlBeanServiceImpl”即可
[========]

##### ↓更多用法请查看下方文档↓

#### [注解与用法（含ID生成、表生成、乐观锁、逻辑删除、连表查询）](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Annotation.md "注解与用法（含ID生成、乐观锁、逻辑删除、连表查询）")
#### [内置Select相关方法](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Select.md "内置Select相关方法")
#### [内置Insert相关方法](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Insert.md "内置Insert相关方法")
#### [内置Delete相关方法](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Delete.md "内置Delete相关方法")
#### [内置Update相关方法](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Update.md "内置Update相关方法")
#### [表结构操作相关方法](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Table.md "表结构操作相关方法")
#### [Service接口和实现类](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Interface.md "Service接口和实现类")
#### [SqlBean和SqlHelper](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/SqlHelper.md "SqlBean和SqlHelper")
#### [Where条件和占位符](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Where.md "Where条件和占位符")
#### [分页查询](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/Paging.md "分页查询")
#### [SqlBeanConfig配置](https://github.com/Jovilam77/vonce-sqlbean/blob/develop/doc/SqlBeanConfig.md "SqlBeanConfig配置")
