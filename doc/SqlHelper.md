#### 说明
```
本插件所有内置方法内部均通过SqlBean + SqlHelper来生成的Sql语句交给Mybatis或Spring JDBC执行，
所以以下例子使用方式同样适用于内置的方法，传递给内置方法的SqlBean可以不设置Sql语句的table，
内部处理的时候发现table不存在的话会自动配置，Select语句连表查询的话仅支持自动补充主表的table。

```
#### 一. SqlHelper 生成Sql语句
```java
   String selectSql =  SqlHelper.buildSelectSql(select);
   String insertSql =  SqlHelper.buildInsertSql(insert);
   String updateSql =  SqlHelper.buildUpdateSql(update);
   String deleteSql =  SqlHelper.buildDeleteSql(delete);
```

#### 二. SqlBeanDB
```java
    SqlBeanDB sqlBeanDB = new SqlBeanDB();
    sqlBeanDB.setDbType(DbType.MySQL);
    sqlBeanDB.setSqlBeanConfig(new SqlBeanConfig());
```

#### 三. Select查询对象
```java
    private static void select1(SqlBeanDB sqlBeanDB) {
        Select select = new Select();
        select.setSqlBeanDB(sqlBeanDB);
        select.setBeanClass(Essay.class);
        select.column(SqlEssay._all);
        select.column(SqlUser.headPortrait, "头像");
        select.column(SqlUser.nickname, "昵称");
        select.setTable(SqlEssay._tableName);
        select.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id, SqlEssay.userId);
        select.where().eq(SqlEssay.userId, "1111");
        //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
        select.where().eq(SqlFun.date_format(SqlEssay.creationTime$, "%Y-%m-%d"), SqlFun.date_format(SqlFun.now(), "%Y-%m-%d"));
        select.orderByDesc(SqlEssay.id);
        System.out.println("---select1---");
        System.out.println(SqlHelper.buildSelectSql(select));
    }

    /**
     * 查询2
     *
     * @param sqlBeanDB
     */
    private static void select2(SqlBeanDB sqlBeanDB) {
        Select select2 = new Select();
        select2.setSqlBeanDB(sqlBeanDB);
        select2.setBeanClass(Essay.class);
        select2.column(SqlEssay.id, "序号")
        .column(SqlEssay.content, "文章内容")
        .column(SqlEssay.creationTime, "创建时间")
        .column(SqlUser.nickname, "用户昵称");
        select2.setTable(SqlEssay._tableName);
        //select2.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id.getName(), SqlEssay.userId.getName());
        //select2.innerJoin(User.class).on(SqlUser.id$, SqlEssay.userId$);
        select2.innerJoin(User.class).on().eq(SqlUser.id$, SqlEssay.userId$).and().gt(SqlUser.id$, 1);
        select2.where().gt(SqlFun.date_format(SqlEssay.creationTime$, "%Y-%m-%d"), "2020-01-01").and().eq(SqlUser.nickname, "vicky");
        System.out.println("---select2---");
        System.out.println(SqlHelper.buildSelectSql(select2));
    }

    /**
     * 查询3
     *
     * @param sqlBeanDB
     */
    private static void select3(SqlBeanDB sqlBeanDB) {
        Select select3 = new Select();
        select3.setSqlBeanDB(sqlBeanDB);
        select3.setBeanClass(Essay.class);
        select3.column(SqlEssay._all, "count")
        .column(SqlEssay.categoryId);
        select3.setTable(SqlEssay._tableName);
        select3.groupBy(SqlEssay.categoryId);
        select3.having().eq("count", 5);
        System.out.println("---select3---");
        System.out.println(SqlHelper.buildSelectSql(select3));
    }

    /**
     * 查询4
     *
     * @param sqlBeanDB
     */
    private static void select4(SqlBeanDB sqlBeanDB) {
        Select select4 = new Select();
        select4.setSqlBeanDB(sqlBeanDB);
        select4.setBeanClass(Essay.class);
        select4.column(SqlUser._all);
        select4.setTable(SqlUser._tableName);
        Integer[] gender = {0, 1};
        select4.where(
        Wrapper.where(Cond.between(SqlUser.id, 2, 6)).
        and(Wrapper.where(Cond.eq(SqlUser.nickname, "vicky")).or(Cond.in(SqlUser.gender, gender))));
        System.out.println("---select4---");
        System.out.println(SqlHelper.buildSelectSql(select4));
    }

    /**
     * 查询5
     *
     * @param sqlBeanDB
     */
    private static void select5(SqlBeanDB sqlBeanDB) {
        Select select5 = new Select();
        select5.setSqlBeanDB(sqlBeanDB);
        select5.setBeanClass(Essay.class);
        select5.column(SqlUser._all);
        select5.setTable(SqlUser._tableName);
        select5.where(Wrapper.where(Cond.eq(SqlUser.id, 1)).and(Wrapper.where(Cond.eq(SqlUser.gender, "1")).or(Cond.eq(SqlUser.nickname, 1))));
        System.out.println("---select5---");
        System.out.println(SqlHelper.buildSelectSql(select5));
    }
```

#### 四. Insert插入对象
```java
    /**
     * 插入1
     *
     * @param sqlBeanDB
     */
    private static void insert1(SqlBeanDB sqlBeanDB) {
        Insert insert = new Insert();
        insert.setBeanClass(User.class);
        insert.setSqlBeanDB(sqlBeanDB);
        User user = new User();
        user.setId("10000");
        user.setUsername("10000");
        user.setNickname("麻花疼");
        user.setHeadPortrait("logo.png");
        user.setGender(0);
        insert.setInsertBean(user);
        System.out.println("---insert1---");
        System.out.println(SqlHelper.buildInsertSql(insert));
    }

    /**
     * 插入2
     *
     * @param sqlBeanDB
     */
    private static void insert2(SqlBeanDB sqlBeanDB) {
        Insert insert = new Insert();
        insert.setSqlBeanDB(sqlBeanDB);
        insert.setBeanClass(User.class);
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
        User user = new User();
        user.setId("id" + i);
        user.setUsername("username" + i);
        user.setNickname("nickname" + i);
        user.setHeadPortrait("logo.png");
        user.setGender(i % 2);
        list.add(user);
        }
        insert.setInsertBean(list);
        System.out.println("---insert2---");
        System.out.println(SqlHelper.buildInsertSql(insert));
    }

    /**
     * 插入3
     *
     * @param sqlBeanDB
     */
    private static void insert3(SqlBeanDB sqlBeanDB) {
        Insert<User> insert = new Insert<>();
        insert.setSqlBeanDB(sqlBeanDB);
        insert.setBeanClass(User.class);
        insert.column(User::getId, User::getGender, User::getNickname).values(1, 2, "Jovi").values(2, 1, "Vicky");
        System.out.println("---insert3---");
        System.out.println(SqlHelper.buildInsertSql(insert));
    }
```

#### 五. Update更新对象
```java
    /**
     * 更新1
     *
     * @param sqlBeanDB
     */
    private static void update(SqlBeanDB sqlBeanDB) {
        User user = new User();
        user.setHeadPortrait("logo.png");
        user.setUsername("123");
        user.setGender(1);
        Update update = new Update();
        update.setTable(User.class);
        update.setSqlBeanDB(sqlBeanDB);
        update.filterFields("username");//java字段名
        update.setUpdateBean(user);
        update.setUpdateNotNull(true);
        update.where().gt(SqlUser.id, 0).and().lt(SqlUser.id, 10);
        System.out.println("---update---");
        System.out.println(SqlHelper.buildUpdateSql(update));
    }

    /**
     * 更新2
     *
     * @param sqlBeanDB
     */
    private static void update2(SqlBeanDB sqlBeanDB) {
        Update<User> update = new Update();
        update.setTable(User.class);
        update.setSqlBeanDB(sqlBeanDB);
        update.set(SqlUser.id, 1).
        set(SqlUser.nickname, "jovi").
        setAdd(SqlUser.integral$, SqlUser.integral$, SqlUser.integral$).
        setSub(User::getGender, User::getGender, 1).
        where().gt(User::getId, 0).and().lt(User::getId, 10);
        System.out.println("---update2---");
        System.out.println(SqlHelper.buildUpdateSql(update));
    }
```

#### 六. Delete删除对象
```java
    /**
     * 删除
     *
     * @param sqlBeanDB
     */
    private static void delete(SqlBeanDB sqlBeanDB) {
        Delete delete = new Delete();
        delete.setTable(User.class);
        delete.setSqlBeanDB(sqlBeanDB);
        delete.where().gt(User::getId, 1).and().eq(User::getNickname, "jovi");
        System.out.println("---delete---");
        System.out.println(SqlHelper.buildDeleteSql(delete));
    }
```
