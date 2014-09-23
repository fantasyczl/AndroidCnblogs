package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * NewsList表
 * Created by ygl on 14-9-23.
 */
public class NewsListTable {
    public static final String TABLE_NAME = "NewsList";

    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [");
        sb.append(TABLE_NAME);
        sb.append("] (");
        sb.append("[NewsId] INTEGER(13) NOT NULL DEFAULT (0), ");
        sb.append("[NewsTitle] NVARCHAR(50) NOT NULL DEFAULT (''), ");
        sb.append("[Summary] NVARCHAR(500) NOT NULL DEFAULT (''), ");
        sb.append("[Content] NTEXT NOT NULL DEFAULT (''), ");
        sb.append("[Published] DATETIME, ");
        sb.append("[Updated] DATETIME, ");
        sb.append("[View] INTEGER(16) DEFAULT (0), ");
        sb.append("[Comments] INTEGER(16) DEFAULT (0), ");
        sb.append("[Digg] INTEGER(16) DEFAULT (0), ");
        sb.append("[IsReaded] BOOLEAN DEFAULT (0), ");
        sb.append("[IsFull] BOOLEAN DEFAULT (0), ");
        sb.append("[CateId] INTEGER(16), ");
        sb.append("[NewsUrl] NVARCHAR(200), ");// 网页地址
        sb.append("[CateName] NVARCHAR(16))");

        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
