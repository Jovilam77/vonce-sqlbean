## BaseController
###### 用来封装一些返回结果的方法，如果你有自己的控制器父类可以继承于这个，或者不想用也可以，如果你想用就往下看
### 自定义结果
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
        PageHelper<Essay> pageHelper = new PageHelper<>(request);
        pageHelper.dispose(testDBService.countAll());
		pageHelper.setDataList(testDBService.selectAll(pageHelper.getPaging()));
        return super.customHint(pageHelper.toResult("获取文章列表成功"));
    }
```
### 成功结果
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
	    //无数据
	    return super.successHint("操作成功");
		//有数据
        //return super.successHint("获取数据成功",testDBService.countAll());
    }
```
### 参数错误
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
	    //无数据
	    return super.parameterHint("参数错误");
		//有数据
        //return super.parameterHint("参数错误","id不能为空");
    }
```
### 系统错误
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
	    return super.errorHint("系统错误");
    }
```
### 权限错误
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
	    return super.unAuthorizedHint("权限错误");
    }
```
### 业务错误
```java
    @RequestMapping(value = "getList", method = RequestMethod.GET)
    @ResponseBody
    public RS getList() {
	    return super.othersHint("业务错误");
    }
```