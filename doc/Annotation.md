## 注解与联表查询
```java
//标识表名
@SqlBeanTable(value="表名")

//标识字段名
@SqlBeanField(value="表字段名",id="是否为id",ignore="是否忽略该字段",isBean="是否为实体类",logically="是否为实体类字段",join=@SqlBeanJoin())

//标识表链接
@SqlBeanJoin(type="内连接还是外连接",table="表名",tableAlias="表别名",tableKeyword="表关键字",mainKeyword="主表关键字",field="查询的字段")

//标识该类是个包装类并继承于某个实体类（主表）
@SqlBeanPojo
```
###### 以上注解属性的类型具体可以看代码文档
#### 单表用法
```java
@SqlBeanTable("d_essay")//标识表名
public class Essay {

	//标识字段名，并指定为id
	@SqlBeanField(value = "id" , id = true)
	private String id;
	
	//标识字段名
	@SqlBeanField(value = "userId" )
	private String userId;

	//标识字段名
	@SqlBeanField(value = "content" )
	private String content;
	
	//标识字段名
	@SqlBeanField(value = "creationTime" )
	private Date creationTime;
	
    /**省略get set方法*/
	
}
```
#### 主外键联表查询的用法
```java
@SqlBeanPojo//标识该类是个包装类并继承于某个实体类（主表）
public class EssayPojo extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//该例子中，userId(数据库字段名)为d_essay表的外键，会自动关联User对象对应的表id
	//如果需要连接的表为主外键关系，那么这样即可完成联表查询
	@SqlBeanField(value = "userId", isBean = true)
	private User user;

	/**省略get set方法*/

}
```
#### 指定某字段关联查询的用法
```java
@SqlBeanPojo//标识该类是个包装类并继承于某个实体类（主表）
public class EssayPojo extends Essay {

	//标识字段名，该字段为一个实体对象，需标识isBean = true
	//@SqlBeanJoin标识需要连接的表以及关联的字段关系
	//field属性标识需要查询的字段，如需查询全部可以忽略或者标识为*
	@SqlBeanField(value = "userId", isBean = true, join = @SqlBeanJoin(table = "d_user", 
	tableKeyword = "id", mainKeyword = "userId",field = {"id","nickname","username"}))
	private User user;

	/**省略get set方法*/

}
```
#### 关联不同字段查询多个字段的用法
```java
@SqlBeanPojo//标识该类是个包装类并继承于某个实体类（主表）
public class EssayPojo extends Essay {

	//查询文章发布者名称
	//标识字段名，这里value = "userNickname"
	//@SqlBeanJoin的用法跟以上例子一样，但是这里需要注意的是
	//因为这里的例子关联用到了两个不同的字段，所以需要设置别名tableAlias = "d_user0"
	//field属性则为需要查询的字段，因为查询的不是一个对象，所以只能设置一个字段
	@SqlBeanField(value = "userNickname", join = @SqlBeanJoin(table = "d_user",
	tableAlias = "d_user0", tableKeyword = "id", mainKeyword = "userId", 
	field = "nickname"))
	private String userNickname;

	//标识字段名，查询文章发布者的头像
	//@SqlBeanJoin的用法跟以上例子一样，但是这里需要注意的是
	//因为这里的例子关联用到了两个不同的字段，所以需要设置别名tableAlias = "d_user0"
	//field属性则为需要查询的字段，因为查询的不是一个对象，所以只能设置一个字段
	@SqlBeanField(value = "userHeadPortrait", join = @SqlBeanJoin(table = "d_user",
	tableAlias = "d_user0", tableKeyword = "id", mainKeyword = "userId", 
	field = "headPortrait"))
	private String userHeadPortrait;

	//标识字段名，查询文章原作者名称（可能不是同一个人）
	//@SqlBeanJoin的用法跟以上例子一样，但是这里需要注意的是
	//因为这里的例子关联用到了两个不同的字段，所以需要设置别名tableAlias = "d_user1"
	//field属性则为需要查询的字段，因为查询的不是一个对象，所以只能设置一个字段
	@SqlBeanField(value = "originalAuthorName", join = @SqlBeanJoin(table = "d_user",
	tableAlias = "d_user1", tableKeyword = "id", mainKeyword = "originalAuthorId", 
	field = "nickname"))
	private String originalAuthorName;

	/**省略get set方法*/

}
```