## Sqlbean

#### 介绍

###### Sqlbean是一款通过Java语法编写SQL语句并自动生成的ORM插件，除了支持Mybatis也同时支持Spring Jdbc，内置大量常用方法，无需编写DAO层，能减少90%的SQL语句，帮助你快速进行业务功能开发。

###### 🚀特点: 无入侵, 多数据源, 动态Schema, 读写分离, 自动建表, 连表查询, 乐观锁, 分页, 支持Spring Jdbc

###### 💻环境: JDK8+, Mybatis3.2.4+, (Spring MVC 4.1.2+, Spring Boot 1.x, Spring Boot 2.x)

###### 💿数据库: Mysql, MariaDB, Oracle, Sqlserver2008+, Postgresql, DB2, Derby, Sqlite, HSQL, H2

###### Sqlbean For Android请移步这里👉 [gitee(推荐)](https://gitee.com/iJovi/vonce-sqlbean-android "vonce-sqlbean-android")、 [github(停止更新)](https://github.com/Jovilam77/vonce-sqlbean-android "vonce-sqlbean-android")

#### 简单上手

###### 1.引入Maven依赖

	<dependency>
		<groupId>cn.vonce</groupId>
		<artifactId>vonce-sqlbean-spring</artifactId>
		<version>1.2.0-beta</version>
	</dependency>

###### 2.标注实体类

```java
@SqlTable("d_user")
public class User {
    @SqlId(type = IdType.SNOWFLAKE_ID_16)
    private Long id;
    private String name;
    private Integer age;
    private Integer stature;
    private Integer gender;
    private String phone;
    private Date createTime;
    /**省略get set方法*/
}
```

###### 3.无需Dao层，Service层接口只需继承SqlBeanService<实体类, id类型>

```java
public interface UserService extends SqlBeanService<User, Long> {
    //这里可以写自己封装的方法

}
```

###### 4.Service实现类只需继承MybatisSqlBeanServiceImpl<实体类, id类型>和实现你的Service接口

```java
//使用Spring Jdbc的话将继承的父类改成SpringJdbcSqlBeanServiceImpl即可
@Service
public class UserServiceImpl extends MybatisSqlBeanServiceImpl<User, Long> implements UserService {

}
```

###### 5.Controller层

```java

@RequestMapping("user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //查询
    @GetMapping("select")
    public RS select() {
        //查询列表
        List<User> list = userService.select();
        list = userService.selectBy(Wrapper.where(gt(User$.id, 10)).and(lt(User$.id, 20)));
        //指定查询
        list = userService.select(new Select().column(User$.id$, User$.name$, User$.phone$).where().gt(User$.id$, 10));

        //查询一条
        User user = userService.selectById(1);
        user = userService.selectOneBy(Wrapper.where(eq(User$.id, 1001)));

        //sql语义化查询《20岁且是女性的用户根据创建时间倒序，获取前10条》
        list = userService.select(new Select().column(User$.id$, User$.name$, User$.phone$).where().eq(User$.age, 22).and().eq(User$.gender, 0).back().orderByDesc(User$.createTime).page(0, 10));

        //联表查询《20岁且是女性的用户根据创建时间倒序，查询前10条用户的信息和地址》
        Select select = new Select();
        select.column(User$.id$, User$.name$, User$.phone$, UserAddress$.province$, UserAddress$.city$, UserAddress$.area$, UserAddress$.details$);
        select.join(JoinType.INNER_JOIN, UserAddress$._tableName, UserAddress$.user_id, User$.id);
        select.where().gt(User$.age$, 22).and().eq(User$.gender$, 0);
        select.orderByDesc(User$.createTime$);
        select.page(0, 10);

        //查询Map
        Map<String, Object> map = userService.selectMap(select);
        List<Map<String, Object>> mapList = userService.selectMapList(select);

        return super.successHint("获取成功", list);
    }

    //分页
    @GetMapping("getList")
    public Map getList(HttpServletRequest request) {
        // 查询对象
        Select select = new Select();
        ReqPageHelper<User> pageHelper = new ReqPageHelper<>(request);
        pageHelper.paging(select, userService);
        return pageHelper.toResult("获取列表成功");
    }

    //更新
    @PostMapping("update")
    public RS update(User user) {
        //根据bean内部id更新
        long i = userService.updateByBeanId(user);
        //根据条件更新
        //i = userService.updateBy(Wrapper.where(gt(User$.age, 22)).and(eq(User$.gender, 1)));
        if (i > 0) {
            return super.successHint("更新成功");
        }
        return super.othersHint("更新失败");
    }

    //删除
    @PostMapping("deleteById")
    public RS deleteById(Integer[] id) {
        //根据id删除
        long i = userService.deleteById(id);
        //根据条件删除
        //i = userService.deleteBy(Wrapper.where(gt(User$.age, 22)).and(eq(User$.gender, 1)));
        if (i > 0) {
            return super.successHint("删除成功");
        }
        return super.othersHint("删除失败");
    }

    //插入
    @PostMapping("add")
    public RS add() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User(i, "name" + i);
            userList.add(user);
        }
        userService.insert(userList);
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
