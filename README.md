## Sqlbean
#### 介绍
###### Sqlbean是一款使用Java面向对象思想来编写并生成Sql语句的工具，在此基础上对Mybatis和Spring Jdbc实现了类似于JPA的轻量级插件支持。其中内置大量常用SQL执行的方法，可以非常方便的达到你想要的目的，相对复杂的SQL语句也得以支持，在常规的项目开发几乎做到不写DAO层，可以有效的提高项目开发的效率，让开发者更专注于业务代码的编写。
 
###### 🚀特点: 零入侵, 多数据源, 动态Schema, 读写分离, 自动建表, 连表查询, 乐观锁, 分页, 支持Mybatis和Spring Jdbc
###### 💻环境: JDK8+, Mybatis3.2.4+, (Spring MVC 4.1.2+, Spring Boot 1.x, Spring Boot 2.x)
###### 💿数据库: Mysql, MariaDB, Oracle, Sqlserver2008+, PostgreSQL, DB2, Derby, Sqlite, HSQL, H2

###### Sqlbean For Android请移步这里👉 [gitee](https://gitee.com/iJovi/vonce-sqlbean-android "vonce-sqlbean-android"), [github](https://github.com/Jovilam77/vonce-sqlbean-android "vonce-sqlbean-android")


#### 简单上手
###### 1.引入Maven依赖
	<dependency>
		<groupId>cn.vonce</groupId>
		<artifactId>vonce-sqlbean-spring</artifactId>
		<version>1.5.4</version>
	</dependency>
###### 2.标注实体类
```java
//标识表名
@SqlTable("d_essay")
public class Essay {

	//标识id字段
	@SqlId(type = IdType.SNOWFLAKE_ID_16)
	//@SqlColumn("id")
	private Long id;

	//@SqlColumn("user_id")
	private String userId;

	//@SqlColumn("content")
	private String content;

	//@SqlColumn("creation_time")
	private Date creationTime;

	//标识乐观锁字段
	@SqlVersion
	//@SqlColumn("update_time")
	private Date updateTime;
	
	/**省略get set方法*/
	
}
```
###### 3.无需Dao层，Service层接口只需继承SqlBeanService<实体类, ID>

```java
public interface EssayService extends SqlBeanService<Essay, Long> {
	//已内置大量常用查询、更新、删除、插入方法，这里可以写自己封装的方法

}
```
###### 4.Service实现类只需继承MybatisSqlBeanServiceImpl<实体类, ID>和实现你的Service接口
```java
//使用Spring Jdbc的话将继承的父类改成SpringJdbcSqlBeanServiceImpl即可
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay, Long> implements EssayService {

	

}
```
###### 5.Controller层
```java
//导入其他需要的包
import cn.vonce.xxx.xxx.XXX;
//导入包装器的条件静态方法，否则需通过Cond.eq("","")获取
import static cn.vonce.sql.helper.Cond.*;

@RequestMapping("essay")
@RestController
public class EssayController {
	
	@Autowired
	private EssayService essayService;

	//查询
	@GetMapping("select")
	public RS select() {

	    //查询列表
	    List<Essay> list = essayService.selectAll();
	    //list = essayService.selectByCondition("& > ?", Essay$.id, 20);
	    list = essayService.selectByCondition(Wrapper.where(gt(Essay$.id, 10)).and(lt(Essay$.id, 20)));

 	    //查询一条
	    Essay essay = essayService.selectById(1L);
	    //essay = essayService.selectOneByCondition("& = ?", Essay$.id, 1);
	    essay = essayService.selectOneByCondition(Wrapper.where(eq(Essay$.id, 333)));

	    //复杂查询
	    Select select = new Select();
	    select.column(SqlEssay.id).column(Essay$.content);
	    //指定查询的表 可不写
	    //select.setTable(Essay.class);
	    //看需求指定连表 这里不演示
	    //select.join("","");
	    select.where().gt("id", 1).and().eq("content", "222");
	    //复杂条件推荐使用
	    //select.setWhere(Wrapper.where(gt(Essay$.id, 1)).and(eq(Essay$.content, "222")));
	    //也可使用表达式 如果这三种条件同时出现 那么此方式优先级最高 上面包装器次之
	    //select.setWhere("& = ? AND & = ?", Essay$.id, 1, Essay$.content, "222");
	    select.orderBy("id", SqlSort.DESC);
	    list = essayService.select(select);
        
	    //用于查询Map
	    Map<String, Object> map = essayService.selectMap(select);

	    //用于查询Map列表
	    List<Map<String, Object>> mapList = essayService.selectMapList(select);
        
	    return super.successHint("获取成功", list);

	}

	//分页
	@GetMapping("getList")
	public Map getList(HttpServletRequest request) {

	    // 查询对象
	    Select select = new Select();
	    ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);
	    pageHelper.paging(select, essayService);
	    return pageHelper.toResult("获取列表成功");

	    // 或者这样
	    // return new PageHelper<Essay>(request).paging(new Select(),essayService).toResult("获取文章列表成功");
        
	    //又或者 更简便的用法（不带统计和页数信息）
	    //List<Essay> list = essayService.selectByCondition(new Paging(0,10), Wrapper.where(gt(Essay$.id, 10)).and(lt(Essay$.id, 20)));
	    //return super.successHint("获取成功", list);
        
	}

	//更新
	@PostMapping("update")
	public RS update(Essay essay) {

	    //根据bean内部id更新
	    long i = essayService.updateByBeanId(essay);
	    //根据外部id更新
	    //i = essayService.updateById(essay, 20);
	    //根据条件更新
	    //i = essayService.updateByCondition(essay, Wrapper.where(gt(Essay$.id, 1)).and(eq(Essay$.content, "222")));

	    if (i > 0) {
               return super.successHint("更新成功");
	    }
	    return super.othersHint("更新失败");
    
	}

	//删除
	@PostMapping("deleteById")
	public RS deleteById(Integer[] id) {

	    //根据id删除
	    long i = essayService.deleteById(id);
	    //根据条件删除
	    //i = essayService.deleteByCondition(Wrapper.where(gt(Essay$.id, 1)).and(eq(Essay$.content, "222")));
	    
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

```


##### 👇👇👇更多用法请查看下方文档👇👇👇

#### 文档说明

###### [0️⃣. 注解详情与使用](doc/Annotation.md "注解详情与使用")
###### [1️⃣. Select](doc/Select.md "Select")
###### [2️⃣. Insert](doc/Insert.md "Insert")
###### [3️⃣. Delete](doc/Delete.md "Delete")
###### [4️⃣. Update](doc/Update.md "Update")
###### [5️⃣. 表操作相关](doc/Table.md "表操作相关")
###### [6️⃣. 分页查询](doc/Paging.md "分页查询")
###### [7️⃣. Service接口和实现类](doc/Interface.md "Service接口和实现类")
###### [8️⃣. SqlBean和SqlHelper](doc/SqlHelper.md "SqlBean和SqlHelper")
###### [9️⃣. Where条件和包装器](doc/Where.md "Where条件和包装器")
###### [🔟. 多数据源动态Schema读写分离相关配置](doc/DataSourceConfig.md "多数据源动态Schema读写分离相关配置")
