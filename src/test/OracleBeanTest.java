
import cn.vonce.sql.bean.*;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.enumerate.SqlLogic;
import cn.vonce.sql.enumerate.SqlOperator;
import cn.vonce.sql.helper.SqlHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OracleBeanTest {

    public static void main(String[] agrs) {
        sql();
    }

    public static void sql() {
        SqlBeanConfig sqlBeanConfig = new SqlBeanConfig();
        sqlBeanConfig.setDbType(DbType.Oracle);
        sqlBeanConfig.setToUpperCase(true);

        long startTime = System.currentTimeMillis();

        // select
        Select select = new Select();
        select.setSqlBeanConfig(sqlBeanConfig);
        select.column("id,name");
        select.setTable(Join.class);
        select.where("name", "jenny");
        select.where(SqlLogic.OR, "", "name", "jovi");
        select.where(SqlLogic.ANDBracket, "", "sex", 1);
        select.setPage(0, 10);
        System.out.println("---select---");
        System.out.println(SqlHelper.buildSelectSql(select));

        // insert
        Insert insert = new Insert();
        insert.setSqlBeanConfig(sqlBeanConfig);
        List<Join> list = new ArrayList<>();
        Join insertJoin = new Join();
        insertJoin.setTableName("name1");
        insertJoin.setMainKeyword("sex1");
        list.add(insertJoin);
        Join insertColumnInfo2 = new Join();
        insertJoin.setTableName("name2");
        insertJoin.setMainKeyword("sex2");
        list.add(insertJoin);
        insert.setInsertBean(list);
        System.out.println("---insert---");
        System.out.println(SqlHelper.buildInsertSql(insert));

        // update
        Update update = new Update();
        update.setSqlBeanConfig(sqlBeanConfig);
        update.setTable("sys_user");
        update.setFilterFields("extra");
        Join updateJoin = new Join();
        updateJoin.setMainKeyword("name");
        updateJoin.setTableName("sex");
        updateJoin.setTableKeyword("123");
        update.setUpdateBean(updateJoin);
        update.setUpdateNotNull(true);
//		update.setWhere("userId = 1111");
        update.where("id", 11);
        System.out.println("---update---");
        System.out.println(SqlHelper.buildUpdateSql(update));

        // delete
        Delete delete = new Delete();
        delete.setSqlBeanConfig(sqlBeanConfig);
        delete.where("", "id", 1, SqlOperator.GREATER_THAN);
        delete.where("name", "jovi");
        delete.setTable("user");
        System.out.println("---delete---");
        System.out.println(SqlHelper.buildDeleteSql(delete));


        float excTime = (float) (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("耗时：" + excTime + "秒");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

}
