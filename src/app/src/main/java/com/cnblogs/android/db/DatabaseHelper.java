package com.cnblogs.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cnblogs.android.GlobalContext;
import com.cnblogs.android.core.Config;
import com.cnblogs.android.db.table.BlogListTable;
import com.cnblogs.android.db.table.CommentListTable;
import com.cnblogs.android.db.table.FavListTable;
import com.cnblogs.android.db.table.NewsListTable;
import com.cnblogs.android.db.table.RssItemTable;
import com.cnblogs.android.db.table.RssListTable;

/**
 * 数据库辅助类
 * Created by ygl on 14-9-23.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // 定义数据库文件
    private static final String DB_NAME = Config.DB_FILE_NAME;
    // 定义数据库版本
    private static final int DB_VERSION = 1;

    private static DatabaseHelper mInstance;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BlogListTable.onCreateTable(db);
        NewsListTable.onCreateTable(db);
        CommentListTable.onCreateTable(db);
        RssListTable.onCreateTable(db);
        RssItemTable.onCreateTable(db);
        FavListTable.onCreateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDropTable(db);
        onCreate(db);
    }

    /**
     * 删除表
     * @param db
     */
    void onDropTable(SQLiteDatabase db) {
        BlogListTable.onDropTable(db);
        NewsListTable.onDropTable(db);
        CommentListTable.onDropTable(db);
        RssListTable.onDropTable(db);
        RssItemTable.onDropTable(db);
        FavListTable.onDropTable(db);
    }

    /**
     * 清空数据表（仅清空无用数据）
     */
    public void clearData() {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM BlogList WHERE IsFull=0 AND BlogId NOT IN(SELECT ContentId FROM FavList WHERE ContentType=0);");// 清空博客表
        sb.append("DELETE FROM NewsList WHERE IsFull=0;");// 清空新闻表
        sb.append("DELETE FROM CommentList;");// 清空评论表
        sb.append("DELETE FROM RssItem;");// 清空订阅文章表
        db.execSQL(sb.toString());
    }


    public static synchronized DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(GlobalContext.getInstance());
        }
        return mInstance;
    }
}
