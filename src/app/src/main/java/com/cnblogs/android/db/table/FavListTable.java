package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建收藏表FavList
 * Created by ygl on 14-9-23.
 */
public class FavListTable {
    public static final String TABLE_NAME = "FavList";

    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [FavList] (");
        sb.append("[FavId] INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("[AddTime] DATETIME NOT NULL DEFAULT (date('now')), ");
        sb.append("[ContentType] INTEGER NOT NULL DEFAULT (0),");
        sb.append("[ContentId] INTEGER NOT NULL DEFAULT (0));");
        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
