package com.lenovo.smarttraffic.entityclass;

public class SqlInfo {
    /**
     * 删除contentItem表
     */
    public static final String CONTENT_DROP = "drop table if exists contentItem";
    /**
     * 创建contentItem库的SQL代码
     **/
    public static final String CONTENT_SQL = "create table contentItem("
            + "id integer primary key autoincrement,"
            + "state int,"
            + "sqrt int,"
            + "itemcontent text)";

    public static final String USERS_USER = "create table userData("
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

    public static final String REACH_SQL = "create table car("
            + "id integer primary key autoincrement, "
            + "CarNumber text,"
            + "Money text,"
            + "User text,"
            + "ReachTime text"
            + ")";

}
