package com.cnblogs.android.dal;
import java.util.List;

import com.cnblogs.android.core.Config;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	public final static byte[] _writeLock = new byte[0];
	public void OpenDB(Context context) {
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	public void Close() {
		dbHelper.close();
		if(db!=null){
			db.close();
		}
	}

	public void Insert(List<ContentValues> list, String tableName) {
		synchronized (_writeLock) {
			db.beginTransaction();
			try {
				db.delete(tableName, null, null);
				for (int i = 0, len = list.size(); i < len; i++)
					db.insert(tableName, null, list.get(i));
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}
	public DBHelper(Context context) {
		this.dbHelper = new DatabaseHelper(context);
	}

	public static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String DB_NAME = Config.DB_FILE_NAME;
		// ������ݿ�汾
		private static final int DB_VERSION = 1;
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			CreateBlogDb(db);
			Log.i("DBHelper", "����BlogList��ɹ�");
			CreateNewsDb(db);
			Log.i("DBHelper", "����NewsList��ɹ�");
			CreateCommentDb(db);
			Log.i("DBHelper", "����CommentList��ɹ�");
			CreateRssListDb(db);
			Log.i("DBHelper", "����RssList��ɹ�");
			CreateRssItemDb(db);
			Log.i("DBHelper", "����RssItem��ɹ�");
			CreateFavListDb(db);
			Log.i("DBHelper", "����FavList��ɹ�");			
		}
		/**
		 * ����BlogList��
		 * 
		 * @param db
		 */
		private void CreateBlogDb(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE [BlogList] (");
			sb.append("[BlogId] INTEGER(13) NOT NULL DEFAULT (0), ");
			sb.append("[BlogTitle] NVARCHAR(50) NOT NULL DEFAULT (''), ");
			sb.append("[Summary] NVARCHAR(500) NOT NULL DEFAULT (''), ");
			sb.append("[Content] NTEXT NOT NULL DEFAULT (''), ");
			sb.append("[Published] DATETIME, ");
			sb.append("[Updated] DATETIME, ");
			sb.append("[AuthorUrl] NVARCHAR(200), ");
			sb.append("[AuthorName] NVARCHAR(50), ");
			sb.append("[AuthorAvatar] NVARCHAR(200), ");
			sb.append("[View] INTEGER(16) DEFAULT (0), ");
			sb.append("[Comments] INTEGER(16) DEFAULT (0), ");
			sb.append("[Digg] INTEGER(16) DEFAULT (0), ");
			sb.append("[IsReaded] BOOLEAN DEFAULT (0), ");
			sb.append("[IsFull] BOOLEAN DEFAULT (0), ");// �Ƿ�ȫ��
			sb.append("[BlogUrl] NVARCHAR(200), ");// ��ҳ��ַ
			sb.append("[UserName] NVARCHAR(50), ");// �û���
			sb.append("[CateId] INTEGER(16), ");
			sb.append("[CateName] NVARCHAR(16))");

			db.execSQL(sb.toString());
		}
		/**
		 * ����NewsList��
		 * 
		 * @param db
		 */
		private void CreateNewsDb(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE [NewsList] (");
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
			sb.append("[NewsUrl] NVARCHAR(200), ");// ��ҳ��ַ
			sb.append("[CateName] NVARCHAR(16))");

			db.execSQL(sb.toString());
		}
		/**
		 * ��������CommentList��
		 * 
		 * @param db
		 */
		private void CreateCommentDb(SQLiteDatabase db) {
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
		/**
		 * �������Ĳ���RssList��
		 * 
		 * @param db
		 */
		private void CreateRssListDb(SQLiteDatabase db) {
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
		/**
		 * ������������RssItem��
		 * 
		 * @param db
		 */
		private void CreateRssItemDb(SQLiteDatabase db) {
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
		/**
		 * �����ղر�FavList
		 * @param db
		 */
		private void CreateFavListDb(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE [FavList] (");
			sb.append("[FavId] INTEGER PRIMARY KEY AUTOINCREMENT,");
			sb.append("[AddTime] DATETIME NOT NULL DEFAULT (date('now')), ");
			sb.append("[ContentType] INTEGER NOT NULL DEFAULT (0),");
			sb.append("[ContentId] INTEGER NOT NULL DEFAULT (0));");
			db.execSQL(sb.toString());
		}
		/**
		 * ���°汾ʱ���±�
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			DropTable(db);
			onCreate(db);
			Log.e("User", "onUpgrade");
		}
		/**
		 * ɾ���
		 * 
		 * @param db
		 */
		private void DropTable(SQLiteDatabase db) {
			StringBuilder sb = new StringBuilder();
			sb.append("DROP TABLE IF EXISTS " + Config.DB_BLOG_TABLE + ";");
			sb.append("DROP TABLE IF EXISTS " + Config.DB_NEWS_TABLE + ";");
			sb.append("DROP TABLE IF EXISTS " + Config.DB_COMMENT_TABLE + ";");
			sb.append("DROP TABLE IF EXISTS " + Config.DB_RSSLIST_TABLE + ";");
			sb.append("DROP TABLE IF EXISTS " + Config.DB_RSSITEM_TABLE + ";");
			sb.append("DROP TABLE IF EXISTS " + Config.DB_FAV_TABLE + ";");
			db.execSQL(sb.toString());
		}
		/**
		 * �����ݱ?�����������ݣ�
		 * @param db
		 */
		public static void ClearData(Context context){
			DatabaseHelper dbHelper = new DBHelper.DatabaseHelper(context);
			SQLiteDatabase db=dbHelper.getWritableDatabase();
			StringBuilder sb=new StringBuilder();
			sb.append("DELETE FROM BlogList WHERE IsFull=0 AND BlogId NOT IN(SELECT ContentId FROM FavList WHERE ContentType=0);");//��ղ��ͱ�
			sb.append("DELETE FROM NewsList WHERE IsFull=0;");//������ű�
			sb.append("DELETE FROM CommentList;");//������۱�
			sb.append("DELETE FROM RssItem;");//��ն������±�
			db.execSQL(sb.toString());
		}
	}
}
