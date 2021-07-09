#### 一. 注解说明和使用
```java
1.@SqlTable     //标识表名
```
属性  | 解释  | 默认 | 必须
 :----: | :-----: | :-----: | :------:  
 autoCreate  | 是否表不存在则自动创建（SqlBeanConfig中含有总开关，默认开启） | true | 否
 constant  | 是否生成实体类对应表的字段常量 | true | 否
 mapUsToCc  | 是否开启Java字段驼峰命名转Sql字段下划线命名 | true | 否
 isView  | 是否为视图 | false | 否
 value  | 表名 |  | 是
 alias  | 别名 | "" | 否
 schema  | schema | "" | 否

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
 value  | 表列字段名 |  | 是
 notNull  | 不是null(创建表使用) | false | 否
 type  | 类型(创建表使用) | JdbcType.NULL | 否
 length  | 长度(创建表使用) | 0 | 否
 decimal  | 小数点(创建表使用) | 0 | 否
 def  | 默认值(创建表使用) | "" | 否
 ignore  | 是否忽略该字段 | false | 否

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


```java
6.@SqlVersion   //标识乐观锁版本，仅支持int、long、Date、Timestamp类型
```

```java
7.@SqlLogically //标识逻辑删除，请配合logicallyDeleteById、logicallyDeleteByCondition这两个方法使用，请查看内置Delete文档
```

```java
8.@SqlInsertTime //标识插入时间，标识的字段在插入时为null将自动赋值
```

```java
9.@SqlUpdateTime //标识更新时间，标识的字段在更新时为null将自动赋值
```

#### 二. 示例.单表用法（该例子已包含表生成、常量生成、id生成、乐观锁、插入时间、更新时间）
```java
@SqlTable("d_essay") //表名
public class Essay {

	@SqlId(type = IdType.UUID) //id生成方式
	@SqlColumn("id") //列字段名
	private String id;

	@SqlColumn("user_id" ) //列字段名, 默认情况下可不写
	private String userId;
	
	@SqlColumn("original_author_id" ) //列字段名, 默认情况下可不写
	private String originalAuthorId;

	@SqlColumn("content" ) //列字段名, 默认情况下可不写
	private String content;
	
	@SqlLogically //逻辑删除
	@SqlColumn("is_deleted" ) //列字段名, 默认情况下可不写
	private Integer isDeleted;
	
	@SqlVersion //乐观锁
	@SqlColumn("version" ) //列字段名, 默认情况下可不写
	private Long version;
	
	@SqlInsertTime
	@SqlColumn("creation_time" ) //列字段名, 默认情况下可不写
	private Date creationTime;
	
	@SqlUpdateTime
	@SqlColumn("update_time" ) //列字段名, 默认情况下可不写
	private Date updateTime;
	
	/**省略get set方法*/
	
}
```

#### 三. 示例.生成的Sql常量（maven编译之后自动生成，不会存在项目中，命名为Sql + 实体类名）
```java
package com.xxx.xxx.model.sql;

import cn.vonce.sql.bean.Column;
import java.lang.String;

public class SqlEssay {
  public static final String _schema = "";

  public static final String _tableName = "d_essay";

  public static final String _tableAlias = "d_essay";

  public static final String _all = "d_essay.*";

  public static final String _count = "COUNT(*)";

  public static final Column id = new Column(_schema,_tableAlias,"id","");

  public static final Column userId = new Column(_schema,_tableAlias,"user_id","");
  
  public static final Column originalAuthorId = new Column(_schema,_tableAlias,"original_author_id","");

  public static final Column content = new Column(_schema,_tableAlias,"content","");

  public static final Column isDeleted = new Column(_schema,_tableAlias,"v","");
  
  public static final Column version = new Column(_schema,_tableAlias,"version","");
  
  public static final Column creationTime = new Column(_schema,_tableAlias,"creation_time","");
  
  public static final Column updateTime = new Column(_schema,_tableAlias,"update_time","");
}
```
#### 四. 示例.主外键联表查询的用法
```java
@SqlUnion//标识该类是个包装类并继承于某个实体类（主表）
public class EssayUnion extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//该例子中，userId(数据库字段名)为d_essay表的外键，会自动关联User对象对应的表id
	//如果需要连接的表为主外键关系，那么这样即可完成联表查询
	@SqlJoin(mainKeyword = "userId", isBean = true)
	private User user;

	/**省略get set方法*/

}
```
#### 五. 示例.指定某字段关联查询的用法
```java
@SqlUnion//标识该类是个包装类并继承于某个实体类（主表）
public class EssayUnion extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//@SqlJoin标识需要连接的表以及关联的字段关系
	//value属性标识需要查询的字段，如需查询全部可以忽略或者标识为*
	@SqlJoin(value = {"id", "nickname", "username"}, table = "d_user",
            tableKeyword = "id", mainKeyword = "userId", isBean = true)
	private User user;

	/**省略get set方法*/

}
```
#### 六. 示例.关联不同字段查询多个字段的用法
```java
@SqlUnion//标识该类是个包装类并继承于某个实体类（主表），该例子连表查询了两个用户信息，所以需要使用表别名
public class EssayUnion extends Essay {

	//查询文章发布者名称
	//注意.这里用到了表别名d_user0
	@SqlJoin(value = "nickname", table = "d_user", tableAlias = "d_user0", tableKeyword = "id", mainKeyword = "userId")
	private String userNickname;

	//查询文章发布者的头像
	//注意.这里用到了表别名d_user0
	@SqlJoin(value = "headPortrait", table = "d_user" tableAlias = "d_user0", tableKeyword = "id", mainKeyword = "userId")
	private String userHeadPortrait;

	//查询文章原作者名称（可能不是同一个人）
	//注意.这里用到了表别名d_user1
	@SqlJoin(value = "nickname", type = JoinType.LEFT_JOIN, table = "d_user", tableAlias = "d_user1", tableKeyword = "id", mainKeyword = "originalAuthorId")
	private String originalAuthorName;

	/**省略get set方法*/

}
```
