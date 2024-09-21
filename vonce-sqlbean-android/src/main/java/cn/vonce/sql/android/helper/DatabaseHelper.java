package cn.vonce.sql.android.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import android.util.Log;
import cn.vonce.sql.android.util.PackageUtil;
import cn.vonce.sql.annotation.SqlTable;
import cn.vonce.sql.bean.Create;
import cn.vonce.sql.bean.Table;
import cn.vonce.sql.config.SqlBeanConfig;
import cn.vonce.sql.config.SqlBeanDB;
import cn.vonce.sql.enumerate.DbType;
import cn.vonce.sql.helper.SqlHelper;
import cn.vonce.sql.uitls.SqlBeanUtil;
import cn.vonce.sql.uitls.StringUtil;

/**
 * 初始化数据库
 *
 * @author Jovi
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String dbName;
    private Class<?> clazz;
    private Context context;

    public DatabaseHelper(Class<?> clazz, Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.dbName = name;
        this.clazz = clazz;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            createTable(db);
        }
    }

    /**
     * 创建表
     *
     * @param db
     */
    public void createTable(SQLiteDatabase db) {
        List<String> classNames = PackageUtil.getClasses(context, clazz.getPackage().getName());
        try {
            Create create;
            SqlBeanDB sqlBeanDB = new SqlBeanDB();
            sqlBeanDB.setDbType(DbType.SQLite);
            sqlBeanDB.setSqlBeanConfig(new SqlBeanConfig());
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                Table table = SqlBeanUtil.getTable(clazz);
                SqlTable sqlTable = clazz.getAnnotation(SqlTable.class);
                if (StringUtil.isEmpty(table.getSchema()) || table.getSchema().equals(dbName)) {
                    if (sqlTable != null && sqlTable.autoCreate()) {
                        create = new Create();
                        create.setSqlBeanDB(sqlBeanDB);
                        create.setBeanClass(clazz);
                        create.setTable(clazz);
                        String sql = SqlHelper.buildCreateSql(create);
                        db.execSQL("DROP TABLE IF EXISTS " + SqlBeanUtil.getTableName(create.getTable(), create));
                        db.execSQL(sql);
                        Log.d("sqlbean", sql);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("sqlbean", e.getMessage(), e);
        }
    }

}
