package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建订阅博客RssList表
 * Created by ygl on 14-9-23.
 */
public class RssListTable {
    public static final String TABLE_NAME = "RssList";

    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [RssList] (");
        sb.append("[RssId] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,");
        sb.append("[Title] NVARCHAR(50) NOT NULL DEFAULT (''),");
        sb.append("[Link] NVARCHAR(500) NOT NULL DEFAULT (''), ");
        sb.append("[Description] NVARCHAR(500) DEFAULT (''),");
        sb.append("[AddTime] DATETIME DEFAULT (date('now')), ");
        sb.append("[OrderNum] INTEGER DEFAULT (0),");
        sb.append("[RssNum] INTEGER DEFAULT (0),");
        sb.append("[Guid] NVARCHAR(500),");
        sb.append("[IsCnblogs] BOOLEAN DEFAULT (0),");
        sb.append("[Image] NVARCHAR(200) DEFAULT (''),");
        sb.append("[Updated] DATETIME DEFAULT (date('now')),");
        sb.append("[Author] NVARCHAR(50) DEFAULT (''),");
        sb.append("[CateId] INTEGER,");
        sb.append("[CateName] NVARCHAR DEFAULT (''),");
        sb.append("[IsActive] BOOLEAN DEFAULT (1));");
        sb.append(");");
        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
