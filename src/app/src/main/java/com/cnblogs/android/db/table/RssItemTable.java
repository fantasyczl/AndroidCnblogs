package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建订阅文章RssItem表
 * Created by ygl on 14-9-23.
 */
public class RssItemTable {
    public static final String TABLE_NAME = "RssItem";

    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [RssItem] (");
        sb.append("[Id] INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("[Title] NVARCHAR(200) DEFAULT (''),");
        sb.append("[Link] NVARCHAR(200) DEFAULT (''),");
        sb.append("[Description] NTEXT DEFAULT (''),");
        sb.append("[Category] NVARCHAR(50),");
        sb.append("[Author] NVARCHAR(50) DEFAULT (''),");
        sb.append("[AddDate] DATETIME,");
        sb.append("[IsReaded] BOOLEAN DEFAULT (0),");
        sb.append("[IsDigg] BOOLEAN DEFAULT (0));");
        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
