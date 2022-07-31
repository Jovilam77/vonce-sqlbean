#### 说明
###### 分页属性，请求时将需要的参数拼上传给后台，new PageHelper<>(request)会自动获取，pagenum从0开始做为第1页
```java
//当前页
private Integer pagenum;
//每页数量
private Integer pagesize;
//排序字段
private String sortdatafield;
//排序方式
private String sortorder;
//时间戳
private String timestamp;
```
#### 一. 返回分页数据包含页数总数信息
```java
@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public RS getList(HttpServletRequest request) {
	Select select = new Select();// 查询对象（具体用法去看Select）
	select.where("id", 1, SqlOperator.GREATER_THAN);//条件
	select.wAND("id", 10, SqlOperator.LESS_THAN);//条件
	select.wORBracket("type", "军事");//条件
	ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);//分页助手，如果你要联表查询请将泛型对象改为你的包装对象（具体请看联表查询注解文档）
	pageHelper.paging(select, essayService);//分页查询
	//如果你要联表查询请使用下面这个（具体请看联表查询注解那里）
	//pageHelper.paging(EssayUnion.class, select, essayService);
	return super.customHint(pageHelper.toResult("获取文章列表成功"));//返回结果
}
```
#### 二. 返回分页数据包含页数总数信息
```java
@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public RS getList(HttpServletRequest request) {
	ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);
	pageHelper.dispose(testDBService.countAll());
	pageHelper.setDataList(testDBService.selectAll(pageHelper.getPaging()));
	//如果你有条件则用这个
	//String sql = "(id > ? and id < ?) or type = ?";
	//Object[] args = new Object[]{1, 10, "军事"};
	//pageHelper.dispose(testDBService.countBy(sql, args));
	//pageHelper.setDataList(testDBService.selectBy(pageHelper.getPaging(), sql, args));
	return super.customHint(pageHelper.toResult("获取文章列表成功"));
}
```
#### 三. 仅返回分页数据
```java
@RequestMapping(value = "getList", method = RequestMethod.GET)
@ResponseBody
public RS getList(HttpServletRequest request) {
	ReqPageHelper<Essay> pageHelper = new ReqPageHelper<>(request);
	return testDBService.selectAll(pageHelper.getPaging());
	//如果你有条件则用这个
	//String sql = "(id > ? and id < ?) or type = ?";
	//Object[] args = new Object[]{1, 10, "军事"};
	//return testDBService.selectBy(pageHelper.getPaging(), sql, args);
}
```
