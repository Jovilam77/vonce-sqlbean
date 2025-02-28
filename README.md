## SqlBean

#### 介绍

###### SqlBean是一款通过Java语法生成SQL语句的ORM插件，其理念是弱化在开发阶段对数据库的操作，无需先建表再开发，可直接编写实体类和业务代码，项目启动后自动创建表和维护表结构。内置大量常用方法，支持灵活的条件查询，无DAO层，在项目中几乎不需要编写SQL语句，帮助你快速进行业务功能开发，除了支持Mybatis也同时支持Spring Jdbc。

###### 🚀特点: 多数据源, 动态Schema, 读写分离, 自动建表, 自动维护表结构, 联表查询, 乐观锁, 分页

###### 💻环境: JDK8+, Mybatis3.2.4+, (Spring MVC 4.1.2+, Spring Boot 1.x, Spring Boot 2.x)

###### 💿数据库: Mysql, MariaDB, Oracle, Sqlserver2008+, Postgresql, DB2, Derby, Sqlite, HSQL, H2

###### SqlBean For Android请移步这里👉 [gitee(推荐)](https://gitee.com/iJovi/vonce-sqlbean-android "vonce-sqlbean-android")、 [github](https://github.com/Jovilam77/vonce-sqlbean-android "vonce-sqlbean-android")

###### SuperCode代码生成项目👉 [https://gitee.com/iJovi/supercode](https://gitee.com/iJovi/supercode "supercode")
###### SqlBean使用例子以及代码生成例子点击这里👉 [https://gitee.com/iJovi/sqlbean-example](https://gitee.com/iJovi/sqlbean-example "sqlbean-example")

#### 快速开始

###### 1.引入Maven依赖

	<dependency>
		<groupId>cn.vonce</groupId>
		<artifactId>vonce-sqlbean-spring</artifactId>
		<version>1.6.4</version>
	</dependency>

###### 2.标注实体类

```java
@Data
public class BaseEntity {

    @SqlId(type = IdType.SNOWFLAKE_ID_16)
    @SqlColumn(notNull = true, remarks = "唯一id")//字段不能为null,表字段注释
    private Long id;

    @SqlColumn(remarks = "创建者")//表字段注释
    private Long creator;

    @SqlDefaultValue(with = FillWith.INSERT)
    @SqlColumn(remarks = "创建时间")//表字段注释
    private Date createTime;

    @SqlColumn(remarks = "更新者")//表字段注释
    private Long updater;

    @SqlDefaultValue(with = FillWith.UPDATE)
    @SqlColumn(remarks = "更新时间")//表字段注释
    private Date updateTime;

    @SqlLogically
    @SqlColumn(remarks = "是否删除(0正常 1删除)")//表字段注释
    private Boolean deleted;

}

@Data
//autoAlter设置为true，实体类有变动时自动同步表结构
@SqlTable(autoAlter = true, value = "t_user", remarks = "用户")
public class User extends BaseEntity {

    @SqlColumn(notNull = true, remarks = "用户名")//字段不能为null,表字段注释
    private String userName;

    @SqlColumn(remarks = "姓名")//表字段注释
    private String nickName;

    @SqlColumn(notNull = true, remarks = "手机号码")//表字段注释
    private String mobilePhone;

    @SqlColumn(notNull = true, remarks = "密码")//表字段注释
    private String password;

    @SqlColumn(notNull = true, remarks = "性别")//表字段注释
    private Integer gender;

    @SqlColumn(remarks = "年龄")//表字段注释
    private Integer age;

    @SqlColumn(remarks = "电子邮箱")//表字段注释
    private String email;

    @SqlColumn(remarks = "头像", oldName = "head_portrait")//表字段注释,修改表字段名称需要在oldName指定旧的名称
    private String avatar;

    @SqlDefaultValue(with = FillWith.INSERT)
    @SqlColumn(notNull = true, remarks = "状态(0正常 1停用)")//字段不能为null,表字段注释
    private UserStatus status;

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
        list = userService.selectBy(Wrapper.where(Cond.gt(User::getId, 10)).and(Cond.lt(User::getId, 20)));
        //指定查询
        list = userService.select(new Select().column(User::getId, User::getUserName, User::getMobilePhone).where().gt(User::getId, 10));

        //查询一条
        User user = userService.selectById(1L);
        user = userService.selectOneBy(Wrapper.where(eq(User::getId, 1001)));

        //sql语义化查询《20岁且是女性的用户根据创建时间倒序，获取前10条》
        list = userService.select(new Select().column(User::getId, User::getUserName, User::getMobilePhone).where().eq(User::getAge, 22).and().eq(User::getGender, 0).back().orderByDesc(User::getCreateTime).page(0, 10));

        //联表查询《20岁且是女性的用户根据创建时间倒序，查询前10条用户的信息和地址》
        Select select = new Select();
        select.column(User::getId, User::getUserName, User::getMobilePhone, UserAddress::getProvince, UserAddress::getCity, UserAddress::getArea, UserAddress::getDetails);
        select.innerJoin(UserAddress.class).on().eq(UserAddress::getId, User::getId);
        select.where().gt(User::getAge, 22).and().eq(User::getGender, 0);
        select.orderByDesc(User::getCreateTime);
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
    
    //统计（函数使用）
    @GetMapping("getStatistics")
    public Result getStatistics() {
        Select select = new Select();
        select.column(SqlFun.count(User::getId), "count").column(SqlFun.avg(User::getAge));
        select.where().gt(SqlFun.date_format(User::getCreateTime, "%Y-%m-%d"), "2024-06-24");
        select.groupBy(User::getGender);
        select.orderByDesc("count");
        List<Map<String, Object>> mapList = userService.selectMapList(select);
        return Result.success(mapList);
    }

    //更新
    @PostMapping("update")
    public RS update(User user) {
        //根据bean内部id更新
        long i = userService.updateByBeanId(user);
        //根据条件更新
        //i = userService.updateBy(Wrapper.where(Cond.gt(User::getAge, 22)).and(Cond.eq(User::getGender, 1)));
        //指定更新某个字段 UPDATE user SET gender = 1, name = 'Jovi' ,age = age + 1 WHERE = id = 111
        userService.update(new Update<User>().set(User::getGender, 1).set(User::getUserName, "Jovi").setAdd(User::getAge, User::getAge, 1).where().eq(User::getId, 111).back());
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
        //i = userService.deleteBy(Wrapper.where(gt(User::getAge, 22)).and(eq(User::getGender, 1)));
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

###### [0️⃣. 注解和枚举使用](doc/Annotation.md "注解和枚举使用")

###### [1️⃣. Select](doc/Select.md "Select")

###### [2️⃣. Insert](doc/Insert.md "Insert")

###### [3️⃣. Delete](doc/Delete.md "Delete")

###### [4️⃣. Update](doc/Update.md "Update")

###### [5️⃣. 数据库相关操作](doc/DbManage.md "数据库相关操作")

###### [6️⃣. 分页查询](doc/Paging.md "分页查询")

###### [7️⃣. Service接口和实现类](doc/Interface.md "Service接口和实现类")

###### [8️⃣. SqlBean和SqlHelper](doc/SqlHelper.md "SqlBean和SqlHelper")

###### [9️⃣. Where条件和Sql函数](doc/Where.md "Where条件和Sql函数")

###### [🔟. 多数据源动态Schema读写分离相关配置](doc/DataSourceConfig.md "多数据源动态Schema读写分离相关配置")
