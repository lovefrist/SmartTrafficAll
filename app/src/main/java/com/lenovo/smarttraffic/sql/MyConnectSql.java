package com.lenovo.smarttraffic.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lenovo.smarttraffic.entityclass.SqlInfo;
/**
 * 数据库类
 * */
public class MyConnectSql extends SQLiteOpenHelper {


    private static int ActivityIDThis;
    private static MyConnectSql connectSQL;


    public static MyConnectSql initMySQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, int ActivityID) {
        connectSQL = new MyConnectSql(context, name, factory, version);
        ActivityIDThis = ActivityID;
        return connectSQL;
    }

    public MyConnectSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        switch (ActivityIDThis) {
            case 1:
                db.execSQL(SqlInfo.CONTENT_SQL);
                break;
            case 2:
                db.execSQL(SqlInfo.USERS_USER);
                break;
            case 3:
                //创建数据库
                Log.d("TAG", "onCreate: 创建数据库");
                db.execSQL(SqlInfo.REACH_SQL);
                break;
            default:
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlInfo.CONTENT_DROP);
        onCreate(db);
    }
}
