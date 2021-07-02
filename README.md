## Sqlbean
#### 介绍
###### Sqlbean是一款使用Java面向对象思想来编写并生成Sql语句的工具，在此基础上对Mybatis和Spring Jdbc实现了类似于JPA的轻量级插件支持。其中内置大量常用SQL执行的方法，可以非常方便的达到你想要的目的，相对复杂的SQL语句也得以支持，在常规的项目开发几乎做到不写DAO层，可以有效的提高项目开发的效率，让开发者更专注于业务代码的编写。

###### 特点：零入侵、多数据源、动态Schema、读写分离、自动建表、连表查询、乐观锁、分页、支持Mybatis和Spring Jdbc
###### 环境：JDK7+，Mybatis3.2.4+，(Spring MVC 4.1.2+ 或 Spring Boot 1.x 或 Spring Boot 2.x)
###### 数据库：Mysql，MariaDB，Oracle，Sqlserver2008+，PostgreSQL，DB2，Derby，Sqlite，HSQL，H2

###### Sqlbean For Android请移步这里👉 [gitee](https://gitee.com/iJovi/vonce-sqlbean-android "vonce-sqlbean-android")， [github](https://github.com/Jovilam77/vonce-sqlbean-android "vonce-sqlbean-android")


#### 简单上手
###### 1：引入Maven依赖
	<dependency>
		<groupId>cn.vonce</groupId>
		<artifactId>vonce-sqlbean-spring</artifactId>
		<version>1.5.0-beta</version>
	</dependency>
###### 2：标注实体类
```java
@SqlTable("d_essay")
public class Essay {

	@SqlId(type = IdType.SNOWFLAKE_ID_16) //标识id字段
	//@SqlColumn("id") 常规情况下可不写
	private Long id;

	//@SqlColumn("user_id" ) 常规情况下可不写
	private String userId;

	//@SqlColumn("content" ) 常规情况下可不写
	private String content;

	//@SqlColumn("creation_time" ) 常规情况下可不写
	private Date creationTime;

    @SqlVersion //标识乐观锁字段
    //@SqlColumn("update_time" ) 常规情况下可不写
	private Date updateTime;
	
	/**省略get set方法*/
	
}
```
###### 3：无需Dao层，Service层接口只需继承SqlBeanService<实体类,ID>

```java
public interface EssayService extends SqlBeanService<Essay,String> {

	//已内置大量常用查询、更新、删除、插入方法，这里可以写自己封装的方法

}
```
###### 4：Service实现类只需继承MybatisSqlBeanServiceImpl<实体类,ID>和实现你的Service接口
```java
//使用Spring Jdbc的话将继承的父类改成SpringJdbcSqlBeanServiceImpl即可
@Service
public class EssayServiceImpl extends MybatisSqlBeanServiceImpl<Essay,String> implements EssayService {

	

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
	@GetMapping("select")
	public RS select() {
		//查询列表  全部
        List<Essay> list = essayService.selectAll();
        //查询列表  根据条件查询 方式一
        list = essayService.selectByCondition("& > ?", SqlEssay.id, 20);
        //查询列表  根据条件查询 方式二 推荐
        list = essayService.selectByCondition(Wrapper.where(Cond.gt(SqlEssay.id, 10)).and(Cond.lt(SqlEssay.id, 20)));


        //查询单条  根据id
        Essay essay = essayService.selectById(1L);
        //查询单条  根据条件查询 方式一
        essay = essayService.selectOneByCondition("& = ?", SqlEssay.id, 1);
        //查询单条  根据条件查询 方式二 推荐
        essay = essayService.selectOneByCondition(Wrapper.where(Cond.eq(SqlEssay.id, 333)));

        //复杂查询
        Select select = new Select();
        //指定查询的字段
        select.column(SqlEssay.id).column(SqlEssay.content);
        //指定查询的表 可不写
        //select.setTable(Essay.class);
        //看需求指定连表 这里不演示
        //select.join("","");
        //id 大于 1  这里的id建议用SqlEssay.id 常量替代 这里演示多种写法特意不写
        select.where("id", 1, SqlOperator.GREATER_THAN);
        //并且 内容等于222 这里的content建议用SqlEssay.content 常量替代 这里演示多种写法特意不写
        select.wAND("content", "222");
        //条件也可用包装器 复杂条件推荐使用
        //select.setWhere(Wrapper.where(Cond.gt(SqlEssay.id, 1)).and(Cond.eq(SqlEssay.content, "222")));
        //也可使用表达式 如果这三种条件同时出现 那么此方式优先级最高 上面包装器次之
        //select.setWhere("& = ? AND & = ?", SqlEssay.id, 1, SqlEssay.content, "222");
        //根据id倒序
        select.orderBy("id", SqlSort.DESC);

        //用于查询Map 多条结果时会报错
        Map<String, Object> map = essayService.selectMap(select);
		//用于查询Map列表
        List<Map<String, Object>> mapList = essayService.selectMapList(select);

        //用于查询对象列表
        list = essayService.select(select);
		
        return super.successHint("获取成功", list);
		//更多用法请查看下方详细文档...
	}

	//分页
	@GetMapping("getList")
	public RS getList(HttpServletRequest request) {
		// 查询对象
        Select select = new Select();
        // 分页助手ReqPageHelper
        ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);
        //分页查询
        pageHelper.paging(select, essayService);
        //返回结果
        return super.customHint(pageHelper.toResult("获取列表成功"));
        // 或者这样
        // return super.customHint(new PageHelper<Essay>(request).paging(new Select(),essayService).toResult("获取文章列表成功"));
        
        //又或者 更简便的用法（不带统计和页数信息）
        //List<Essay> list = essayService.selectByCondition(new Paging(0,10), Wrapper.where(Cond.gt(SqlEssay.id, 10)).and(Cond.lt(SqlEssay.id, 20)));
        //return super.successHint("获取成功", list);
	}

	//更新
	@PostMapping("update")
	public RS update(Essay essay) {
	    //根据bean内部id更新
		long i = essayService.updateByBeanId(essay);
		//根据外部id更新 参数3的true代表仅更新不为null字段 参数4的true代表使用乐观锁
        //i = essayService.updateById(essay,20,true,true);
		//根据条件更新 参数2的true代表仅更新不为null字段 参数3的true代表使用乐观锁
        //i = essayService.updateByCondition(essay,true,true,Wrapper.where(Cond.gt(SqlEssay.id, 1)).and(Cond.eq(SqlEssay.content, "222")));
		//更多用法请查看下方详细文档...
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
        //i = essayService.deleteByCondition(Wrapper.where(Cond.gt(SqlEssay.id, 1)).and(Cond.eq(SqlEssay.content, "222")));
        //更多用法请查看下方详细文档...
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


##### ↓↓↓更多用法请查看下方文档↓↓↓

#### 文档说明

###### [1. 注解详情与使用](doc/Annotation.md "注解与用法")
###### [2. Select](doc/Select.md "内置Select相关方法")
###### [3. Insert](doc/Insert.md "内置Insert相关方法")
###### [4. Delete](doc/Delete.md "内置Delete相关方法")
###### [5. Update](doc/Update.md "内置Update相关方法")
###### [6. 表操作相关](doc/Table.md "表结构操作相关方法")
###### [7. 分页查询](doc/Paging.md "分页查询")
###### [8. Service接口和实现类](doc/Interface.md "Service接口和实现类")
###### [9. SqlBean和SqlHelper](doc/SqlHelper.md "SqlBean和SqlHelper")
###### [10. Where条件和占位符](doc/Where.md "Where条件和占位符")
###### [11. 数据源相关配置](doc/SqlBeanConfig.md "SqlBeanConfig配置")
