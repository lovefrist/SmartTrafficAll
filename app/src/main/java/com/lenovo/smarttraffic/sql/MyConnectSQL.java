package com.lenovo.smarttraffic.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyConnectSQL extends SQLiteOpenHelper {
    /**
     * 删除contentItem表
     */
    private static final String CONTENT_DROP = "drop table if exists contentItem";
    /**
     * 创建contentItem库的SQL代码
     **/
    private static final String CONTENT_SQL = "create table contentItem("
            + "id integer primary key autoincrement,"
            + "state int,"
            + "sqrt int,"
            + "itemcontent text)";

    private static final String USERS_USER = "create table userData("
            + "id integer primary key autoincrement, "
            + "UserName text,"
            + "AdminName text,"
            + "Phone text,"
            + "Time text,"
            + "Admin text,"
            + "imgUri text,"
            + "psex text,"
            + "state int,"
            + "StateTop int"
            + ")";

    private static int ActivityIDThis;
    private static MyConnectSQL connectSQL;
    private Context context;

    public static MyConnectSQL initMySQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, int ActivityID) {
        connectSQL = new MyConnectSQL(context, name, factory, version);
        ActivityIDThis = ActivityID;
        return connectSQL;
    }

    public MyConnectSQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        switch (ActivityIDThis) {
            case 1:
                db.execSQL(CONTENT_SQL);
                break;
            case 2:
                db.execSQL(USERS_USER);
                break;

            default:
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CONTENT_DROP);
        onCreate(db);
    }
}
