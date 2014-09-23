package com.cnblogs.android.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * 博客列表
 * Created by ygl on 14-9-23.
 */
public class BlogListTable {
    public static final String TABLE_NAME = "BlogList";

    public static final String BlogId = "BlogId";
    public static final String BlogTitle = "BlogTitle";
    public static final String Summary = "Summary";
    public static final String Content = "Content";
    public static final String Published = "Published";
    public static final String Updated = "Updated";
    public static final String AuthorUrl = "AuthorUrl";
    public static final String AuthorName = "AuthorName";
    public static final String AuthorAvatar = "AuthorAvatar";
    public static final String View = "View";
    public static final String Comments = "Comments";
    public static final String Digg = "Digg";
    public static final String IsReaded = "IsReaded";
    public static final String IsFull = "IsFull";
    public static final String BlogUrl = "BlogUrl";
    public static final String UserName = "UserName";
    public static final String CateId = "CateId";
    public static final String CateName = "CateName";


    public static void onCreateTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE [");
        sb.append(TABLE_NAME);
        sb.append("] ([");
        sb.append(BlogId);
        sb.append("] INTEGER(13) NOT NULL DEFAULT (0), [");
        sb.append(BlogTitle);
        sb.append("] NVARCHAR(50) NOT NULL DEFAULT (''), [");
        sb.append(Summary);
        sb.append("] NVARCHAR(500) NOT NULL DEFAULT (''), [");
        sb.append(Content);
        sb.append("] NTEXT NOT NULL DEFAULT (''), [");
        sb.append(Published);
        sb.append("] DATETIME, [");
        sb.append(Updated);
        sb.append("] DATETIME, [");
        sb.append(AuthorUrl);
        sb.append("] NVARCHAR(200), [");
        sb.append(AuthorName);
        sb.append("] NVARCHAR(50), [");
        sb.append(AuthorAvatar);
        sb.append("] NVARCHAR(200), [");
        sb.append(View);
        sb.append("] INTEGER(16) DEFAULT (0), [");
        sb.append(Comments);
        sb.append("] INTEGER(16) DEFAULT (0), [");
        sb.append(Digg);
        sb.append("] INTEGER(16) DEFAULT (0), [");
        sb.append(IsReaded);
        sb.append("] BOOLEAN DEFAULT (0), [");
        sb.append(IsFull);
        sb.append("] BOOLEAN DEFAULT (0), [");// 是否全文
        sb.append(BlogUrl);
        sb.append("] NVARCHAR(200), [");// 网页地址
        sb.append(UserName);
        sb.append("] NVARCHAR(50), [");// 用户名
        sb.append(CateId);
        sb.append("] INTEGER(16), [");
        sb.append(CateName);
        sb.append("] NVARCHAR(16))");

        db.execSQL(sb.toString());
    }

    public static void onDropTable(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        db.execSQL(dropSql);
    }
}
