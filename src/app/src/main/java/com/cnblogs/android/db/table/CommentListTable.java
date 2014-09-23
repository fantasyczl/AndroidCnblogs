package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * 创建评论CommentList表
 * Created by ygl on 14-9-23.
 */
public class CommentListTable {
    public static final String TABLE_NAME = "CommentList";

    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [CommentList] (");
        sb.append("[CommentId] INTEGER NOT NULL DEFAULT (0), ");
        sb.append("[PostUserUrl] NVARCHAR(200) NOT NULL DEFAULT (''), ");
        sb.append("[PostUserName] NVARCHAR(50) NOT NULL DEFAULT (''), ");
        sb.append("[Content] NTEXT NOT NULL DEFAULT (''), ");
        sb.append("[ContentId] INTEGER NOT NULL DEFAULT (0), ");
        sb.append("[CommentType] INTEGER DEFAULT (0), ");
        sb.append("[AddTime] DATETIME);");
        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
