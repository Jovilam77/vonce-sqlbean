#### 一. 注解说明和使用
```java
1.@SqlTable     //标识表名
```
属性  | 解释  |  默认   | 必须
 :----: | :-----: |:-----:| :------:  
 autoCreate  | 是否表不存在则自动创建（SqlBeanConfig中含有总开关，默认开启） | true  | 否
 autoAlter  | 是否自动更改表结构 | false | 否
 constant  | 是否生成实体类对应表的字段常量 | true  | 否
 mapUsToCc  | 是否开启Java字段驼峰命名转Sql字段下划线命名 | true  | 否
 isView  | 是否为视图 | false | 否
 value  | 表名 |       | 是
 alias  | 表别名 |  ""   | 否
 schema  | schema |  ""   | 否
 remarks  | 表注释 |  ""   | 否

```java
2.@SqlId     //标识id，目前仅支持UUID, SNOWFLAKE_ID_16, SNOWFLAKE_ID_18，请查看IdType枚举类
```

属性  | 解释  | 默认 | 必须
 :----: | :-----: | :-----: | :------: 
 type  | 生成类型 | IdType.NORMAL | 否

```java
3.@SqlColumn     //标识字段名
```

属性  | 解释  | 默认 | 必须
 :----: | :-----: | :-----: | :------: 
 value  | 表列字段名 | "" | 否
 notNull  | 不是null(创建表使用) | false | 否
 type  | 类型(创建表使用) | JdbcType.NULL | 否
 length  | 长度(创建表使用) | 0 | 否
 decimal  | 小数点(创建表使用) | 0 | 否
 def  | 默认值(创建表使用) | "" | 否
 remarks  | 字段注释 | "" | 否
 ignore  | 是否忽略该字段 | false | 否
 oldName  | 旧字段名称 | "" | 否

```java
4.@SqlUnion     //标识表连接并继承于某个实体类（主表）
```

```java
5.@SqlJoin       //标识表连接
```

属性  | 解释  | 默认 | 必须
 :----: | :-----: | :-----: | :------: 
 value  | 连接查询表列字段名 | "" | 否
 isBean  | 是否为一个实体类 | false | 否
 type  | 连接类型 | JoinType.INNER_JOIN |否
 schema | 连接的schema | "" |否
 table | 连接的表名 | "" |否
 tableAlias | 连接表的别名 | "" |否
 tableKeyword | 连接的表字段 | "" |否
 mainKeyword | 主表的字段 | "" |否
 from | 连接的表（通常情况下可代替schema、table、tableAlias，优先级高） | void.class |否
 on | 连接条件（优先级高） | void.class |否

```java
6.@SqlDefaultValue //标识该注解的字段如果为null自动注入默认值（仅支持基本类型、String、Date、Timestamp、BigDecimal）
```
属性  | 解释  | 默认 | 必须
 :----: | :-----: | :-----: | :------: 
with  | 填充类型（insert=新增、update=更新，together=新增更新同时） |  | 是

```java
7.@SqlVersion   //标识乐观锁版本，仅支持int、long、Date、Timestamp类型
```

```java
8.@SqlLogically //标识逻辑删除，请配合logicallyDeleteById、logicallyDeleteBy这两个方法使用，请查看内置Delete文档
```

#### 二. 示例.单表用法（该例子已包含表生成、常量生成、id生成、乐观锁、插入时间、更新时间）
```java
@Data
@SqlTable("d_essay") //表名
public class Essay {

	@SqlId(type = IdType.UUID) //id生成方式
	private String id;

	private String userId;
	
	private String originalAuthorId;

	private String content;
	
	@SqlLogically //逻辑删除
	private Integer isDeleted;
	
	@SqlVersion //乐观锁
	private Long version;

	@SqlDefaultValue(with = FillWith.INSERT)
	private Date creationTime;

	@SqlDefaultValue(with = FillWith.UPDATE)
	private Date updateTime;
	
}
```

#### 三. 示例.生成的Sql常量（maven编译之后自动生成，不会存在项目中，命名为Sql + 实体类名）
```java
package com.xxx.xxx.model.sql;

import cn.vonce.sql.bean.Column;
import java.lang.String;

public class Essay$ {

  public static final String _schema = "";

  public static final String _tableName = "d_essay";

  public static final String _tableAlias = "d_essay";

  public static final String _all = "d_essay.*";

  public static final String _count = "COUNT(*)";

  public static final String id = "id";

  public static final Column id$ = new Column(_schema,_tableAlias,"id","");

  public static final String userId = "user_id";

  public static final Column userId$ = new Column(_schema,_tableAlias,"user_id","");

  public static final String originalAuthorId = "original_author_id";
  
  public static final Column originalAuthorId$ = new Column(_schema,_tableAlias,"original_author_id","");

  public static final String content= "content";

  public static final Column content$ = new Column(_schema,_tableAlias,"content","");

  public static final String isDeleted = "is_deleted";

  public static final Column isDeleted$ = new Column(_schema,_tableAlias,"is_deleted","");

  public static final String version= "version";
  
  public static final Column version$ = new Column(_schema,_tableAlias,"version","");

  public static final String creationTime = "creation_time";
  
  public static final Column creationTime$ = new Column(_schema,_tableAlias,"creation_time","");

  public static final String updateTime = "update_time";
  
  public static final Column updateTime$ = new Column(_schema,_tableAlias,"update_time","");

}
```
#### 四. 示例.主外键联表查询的用法
```java
public class EssayUnion extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//该例子中，userId(数据库字段名)为d_essay表的外键，会自动关联User对象对应的表id
	//如果需要连接的表为主外键关系，那么这样即可完成联表查询
	@SqlJoin(mainKeyword = Essay$.userId, isBean = true)
	private User user;

	/**省略get set方法*/

}

@RestController
public class XxxController {

    @Autowired
    private EssayService essayService;

    @GetMapper("getList")
    public Object getList(){
        return essayService.select(EssayUnion.class);//最后查询时需指定类型EssayUnion.class，所有selectXX方法都支持
    }

}
```
#### 五. 示例.指定某字段关联查询的用法
```java
public class EssayUnion extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//@SqlJoin标识需要连接的表以及关联的字段关系
	//value属性标识需要查询的字段，如需查询全部可以忽略或者标识为*
	@SqlJoin(value = {$User.id, $User.nickname, $User.username}, table = $User._tableName ,
            tableKeyword = $User.id, mainKeyword = Essay$.userId, isBean = true)
	private User user;

	/**省略get set方法*/

}

@RestController
public class XxxController {

    @Autowired
    private EssayService essayService;

    @GetMapper("getList")
    public Object getList(){
        return essayService.select(EssayUnion.class);//最后查询时需指定类型EssayUnion.class，所有selectXX方法都支持
    }

}
```
#### 六. 示例.关联不同字段查询多个字段的用法
```java
public class EssayUnion extends Essay {

	//查询文章发布者名称
	//注意.这里用到了表别名d_user0
	@SqlJoin(value = $User.nickname, table = $User._tableName, tableAlias = "user0", tableKeyword = $User.id, mainKeyword = Essay$.userId)
	private String userNickname;

	//查询文章发布者的头像
	//注意.这里用到了表别名d_user0
	@SqlJoin(value = $User.headPortrait, table = $User._tableName, tableAlias = "user0", tableKeyword = $User.id, mainKeyword = Essay$.userId)
	private String userHeadPortrait;

	//查询文章原作者名称（可能不是同一个人）
	//注意.这里用到了表别名d_user1
	@SqlJoin(value = $User.nickname, type = JoinType.LEFT_JOIN, table = $User._tableName, tableAlias = "user1", tableKeyword = $User.id, mainKeyword = Essay$.originalAuthorId)
	private String originalAuthorName;

	/**省略get set方法*/

}

@RestController
public class XxxController {
    
    @Autowired
    private EssayService essayService;
    
    @GetMapper("getList")
    public Object getList(){
        return essayService.select(EssayUnion.class);//最后查询时需指定类型EssayUnion.class，所有selectXX方法都支持
    }
    
}
```
#### 七. 枚举使用实例
```
使用枚举需要在枚举中实现SqlEnum接口
```
```java
public enum UserStatus implements SqlEnum<Integer> {
    DISABLE(0, "禁用"), NORMAL(1, "正常");

    UserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Integer code;
    private String desc;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }

}
```