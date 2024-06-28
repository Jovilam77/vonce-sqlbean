package cn.vonce.sql.test;

import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.define.SqlFun;
import cn.vonce.sql.enumerate.*;
import cn.vonce.sql.helper.Cond;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.helper.Wrapper;
import cn.vonce.sql.model.Essay;
import cn.vonce.sql.model.User;
import cn.vonce.sql.model.union.EssayUnion;
import cn.vonce.sql.uitls.SqlBeanUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * sql语句生成测试
 * 测试中写的sql，可能某些字段的条件写的不符合现实逻辑，不要在意，只是为了测试语法支持。
 * 实例仅供参考，可自由发挥写出符合自己业务需要的sql语句
 */
public class SqlHelperTest {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        SqlBeanDB sqlBeanDB = new SqlBeanDB();
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setToUpperCase(false);
        sqlBeanDB.setDbType(DbType.MySQL);
        sqlBeanDB.setSqlBeanConfig(sqlBeanConfig);

        long startTime = System.currentTimeMillis();

        // select1
        select1(sqlBeanDB);

        // select2
        select2(sqlBeanDB);

        // select3
        select3(sqlBeanDB);
//
//        // select4
//        select4(sqlBeanDB);
//
//        // select5
//        select5(sqlBeanDB);
//
//        // insert1
//        insert1(sqlBeanDB);
//
//        // insert2
//        insert2(sqlBeanDB);
//
//        // update
//        update(sqlBeanDB);
//
//        // update2
//        update2(sqlBeanDB);
//
//        // delete
//        delete(sqlBeanDB);

        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");
    }

    /**
     * 查询1
     *
     * @param sqlBeanDB
     */
    private static void select1(SqlBeanDB sqlBeanDB) {
        Select select = new Select();
        select.setSqlBeanDB(sqlBeanDB);
        select.setBeanClass(Essay.class);
        select.column(Essay::getClass);
        select.column(User::getHeadPortrait, "头像");
        select.column(User::getNickname, "昵称");
        select.setTable(Essay.class);
        select.innerJoin(User.class).on(User::getId, Essay::getUserId);
        select.where().eq(Essay::getClass, "1111");
        //value 直接输入字符串 会当作字符串处理，sql中会带''，如果希望不被做处理则使用Original
        select.where().eq(SqlFun.date_format(Essay::getCreationTime, "%Y-%m-%d"), SqlFun.date_format(SqlFun.now(), "%Y-%m-%d"));
        select.orderByDesc(Essay::getId);
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
        select2.column(Essay::getId, "序号")
                .column(Essay::getContent, "文章内容")
                .column(Essay::getCreationTime, "创建时间")
                .column(User::getNickname, "用户昵称");
        select2.setTable(Essay.class);
//        select2.join(JoinType.INNER_JOIN, SqlUser._tableAlias, SqlUser.id.getName(), Essay.userId.getName());
//        select2.innerJoin(User.class).on(SqlUser.id$, Essay.userId$);
        select2.innerJoin(User.class).on().eq(User::getId, Essay::getUserId).and().gt(User::getId, 1);
        select2.where().gt(SqlFun.date_format(Essay::getCreationTime, "%Y-%m-%d"), "2020-01-01").and().eq(User::getNickname, "vicky");
        System.out.println("---select2---");
        System.out.println(SqlHelper.buildSelectSql(select2));
    }


    /**
     * 查询3
     *
     * @param sqlBeanDB
     */
    private static void select3(SqlBeanDB sqlBeanDB) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Select select3 = new Select();
        select3.setSqlBeanDB(sqlBeanDB);
        select3.setBeanClass(Essay.class);
        select3.column(SqlFun.count(Essay::getId), "count")
                .column(Essay::getCategoryId);
        select3.setTable(Essay.class);
        SqlBeanUtil.setJoin(select3,EssayUnion.class);
        select3.groupBy(Essay::getCategoryId);
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
        select4.column(User::getClass);
        select4.setTable(User.class);
        Integer[] gender = {0, 1};
        select4.where(
                Wrapper.where(Cond.between(User::getId, 2, 6)).
                        and(Wrapper.where(Cond.eq(User::getNickname, "vicky")).or(Cond.in(User::getGender, gender))));
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
        select5.column(User::getClass);
        select5.setTable(User.class);
        select5.where(Wrapper.where(Cond.eq(User::getId, 1)).and(Wrapper.where(Cond.eq(User::getGender, "1")).or(Cond.eq(User::getNickname, 1))));
        System.out.println("---select5---");
        System.out.println(SqlHelper.buildSelectSql(select5));
    }

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
        insert.setBean(user);
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
        insert.setBean(list);
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
        Update<User> update = new Update();
        update.setTable(User.class);
        update.setSqlBeanDB(sqlBeanDB);
        update.filterFields("username").bean(user).notNull(true);
        update.where().gt(User::getId, 0).and().lt(User::getId, 10);
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
        update.set(User::getId, 1).
                set(User::getNickname, "jovi").
                setAdd(User::getIntegral, User::getIntegral, new RawValue(User::getIntegral)).
                setSub(User::getGender, User::getGender, 1).
                where().gt(User::getId, 0).and().lt(User::getId, 10);
        System.out.println("---update2---");
        System.out.println(SqlHelper.buildUpdateSql(update));
    }

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

}