import cn.vonce.generate.bean.ColumnInfo;
import cn.vonce.sql.bean.Delete;
import cn.vonce.sql.bean.Insert;
import cn.vonce.sql.bean.Select;
import cn.vonce.sql.bean.Update;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.enumerate.SqlSort;
import cn.vonce.sql.exception.SqlBeanException;
import cn.vonce.sql.helper.SqlHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqlHelperTest {

    public static void main(String[] args) throws SqlBeanException {
        long startTime = System.currentTimeMillis();

        // select
        Select select = new Select();
        select.setColumn("essay_comment.*", "user.headPortrait", "user.nickname");
        select.setFrom("`essay_comment`");
        select.innerJoin("user", "user.id", "essay_comment.userId");
        select.where("#essay_comment.essayId", "22essay_comment.essayId", SqlOperator.GREATER_THAN);
        select.where(SqlLogic.AND, "essay_comment.essayId", 1, SqlOperator.LESS_THAN).where(SqlLogic.AND, "essay_comment.userId", "1111")
                .where(SqlLogic.ORBracket, "user.nickname", "jovi");
        select.groupBy("id");
        select.having("sum(price)", 2999, ">");
        select.orderBy("user.id", SqlSort.DESC).orderBy("essay_comment.userId", SqlSort.ASC);
        System.out.println("---select---");
        select.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildSelectSql(select));

        // select2
        Select select2 = new Select();
        select2.column("*");
        select2.setFrom("user");
        select2.where("name", "jovi");
        select2.where(SqlLogic.OR, "name", "vicky");
        select2.where(SqlLogic.ANDBracket, "sex", 1);
        System.out.println("---select2---");
        select2.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildSelectSql(select2));

        // select3
        Select select3 = new Select();
        select3.column("*");
        select3.setFrom("user");
        // String[] between = { "1", "5" };
        List<Object> stringList = new ArrayList<>();
        stringList.add(2);
        stringList.add("6");
        select3.where("id", stringList, SqlOperator.BETWEEN);
        // MysqlBean3.where("name", "vicky");
        select3.where(SqlLogic.AND, "sex", stringList, SqlOperator.IN);
        System.out.println("---select3---");// where id between 1 and 2
        select3.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildSelectSql(select3));

        // select4
        Select select4 = new Select();
        select4.column("*");
        select4.setFrom("user");
        // String[] between = { "1", "5" };
        List<Object> stringList2 = new ArrayList<>();
        stringList2.add(2);
        stringList2.add("6");
        select4.where("id", stringList2, SqlOperator.BETWEEN);
        select4.wANDBracket("name", "vicky", SqlOperator.EQUAL_TO);
        select4.wOR("sex", stringList2, SqlOperator.IN);
        System.out.println("---select4---");// where id between 1 and 2
        select4.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildSelectSql(select4));

        // update
        Update update = new Update();
        update.setUpdateTable("sys_user");
        update.setFilterFields("extra");
        ColumnInfo updateColumnInfo = new ColumnInfo();
        updateColumnInfo.setColumnName("name");
        updateColumnInfo.setComments("sex");
        updateColumnInfo.setExtra("123");
        update.setUpdateBean(updateColumnInfo);
        update.setUpdateNotNull(true);
//		update.setWhere("userId = 1111");
        update.where("id", 11);
        System.out.println("---update---");
        update.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildUpdateSql(update));

        // update 2
        Update update2 = new Update();
        update2.setUpdateTable("sys_user");
        update2.setUpdateBean(updateColumnInfo);
//		update2.where("id",11);
        System.out.println("---update 2---");
        update2.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildUpdateSql(update2));


        // insert
        Insert insert = new Insert();
        List<ColumnInfo> list = new ArrayList<>();
        ColumnInfo insertColumnInfo = new ColumnInfo();
        insertColumnInfo.setColumnName("name1");
        insertColumnInfo.setComments("sex1");
        list.add(insertColumnInfo);
        ColumnInfo insertColumnInfo2 = new ColumnInfo();
        insertColumnInfo2.setColumnName("name2");
        insertColumnInfo2.setComments("sex2");
        list.add(insertColumnInfo2);
        insert.setInsertBean(list);
        System.out.println("---insert---");
        insert.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildInsertSql(insert));

        // delete
        Delete delete = new Delete();
        delete.where("id", 1, ">");
        delete.where("name", "jovi");
        delete.setDeleteBable("user");
        System.out.println("---delete---");
        delete.setSqlBeanConfig(new SqlBeanConfig(DbType.MySQL));
        System.out.println(SqlHelper.buildDeleteSql(delete));

        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Object o = new Date();
        System.out.println(sdf.format(o));
    }

}
