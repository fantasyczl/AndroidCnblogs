package com.cnblogs.android.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cnblogs.android.db.table.BlogListTable;
import com.cnblogs.android.entity.Blog;
import com.cnblogs.android.utility.TimeTools;

import java.util.ArrayList;
import java.util.List;

public class BlogListDBTask {

    private static SQLiteDatabase getRsd() {
        return DatabaseHelper.getInstance().getReadableDatabase();
    }

    private static SQLiteDatabase getWsd() {
        return DatabaseHelper.getInstance().getWritableDatabase();
    }

	private static boolean isExist(int blogId) {
		String where = BlogListTable.BlogId + "=?";
		String[] args = {String.valueOf(blogId)};
		Cursor cursor = getRsd().query(BlogListTable.TABLE_NAME, null, where, args, null,
                null, null);
		boolean isExist = cursor != null && cursor.moveToNext();

        if (cursor != null)
		    cursor.close();

		return isExist;
	}

	private static boolean isFull(int blogId){
		String where = BlogListTable.BlogId + "=?";
		String[] args = {String.valueOf(blogId)};
		Cursor cursor = getRsd().query(BlogListTable.TABLE_NAME, null, where, args, null,
                null, null);
		
		if(!cursor.moveToNext() || cursor.getColumnIndex(BlogListTable.IsFull) < 0){
			cursor.close();
			return false;
		}

        String str = cursor.getString(cursor.getColumnIndex(BlogListTable.IsFull));
		boolean isFull = !TextUtils.isEmpty(str) && "1".equals(str);

		cursor.close();
		return isFull;
	}

	public List<Blog> getTopBlogList() {
		String limit = "10";
		String where = "";

		return getBlogListByWhere(limit, where, null);
	}
	
	
	public static List<Blog> getBlogListByPage(int pageIndex, int pageSize) {
        String limit = String.format("%d,%d", (pageIndex - 1) * pageSize, pageSize);
		return getBlogListByWhere(limit, null, null);
	}

	public static List<Blog> getBlogListByAuthor(String author, int pageIndex, int pageSize){
		String limit = String.valueOf((pageIndex - 1) * pageSize) + "," + String.valueOf(pageSize);
		String where="AuthorName=?";
		String[] args={author};
		List<Blog> list = getBlogListByWhere(limit, where, args);
		
		return list;
	}

	public static Blog getBlogEntity(int blogId) {
		String limit = "1";
		String where = "BlogId=?";
		String[] args = {String.valueOf(blogId)};
		List<Blog> list = getBlogListByWhere(limit, where, args);
		if (list.size() > 0) {
			return list.get(0);
		}

		return null;
	}

	public static List<Blog> getBlogListByWhere(String limit, String where, String[] args) {
		List<Blog> listBlog = new ArrayList<Blog>();
		String orderBy = BlogListTable.BlogId + " desc";//"BlogID desc";
		Cursor cursor = getRsd().query(BlogListTable.TABLE_NAME, null, where, args, null, null, orderBy, limit);

		while (cursor != null && cursor.moveToNext()) {
			Blog entity = new Blog();
			entity.setAddTime(cursor.getString(cursor.getColumnIndex(BlogListTable.Published)));
			entity.SetAuthor(cursor.getString(cursor.getColumnIndex(BlogListTable.AuthorName)));
			entity.SetAuthorUrl(cursor.getString(cursor.getColumnIndex(BlogListTable.AuthorUrl)));
			entity.SetAvator(cursor.getString(cursor.getColumnIndex(BlogListTable.AuthorAvatar)));
			entity.SetBlogContent(cursor.getString(cursor.getColumnIndex(BlogListTable.Content)));
			entity.SetBlogId(cursor.getInt(cursor.getColumnIndex(BlogListTable.BlogId)));
			entity.SetBlogTitle(cursor.getString(cursor.getColumnIndex(BlogListTable.BlogTitle)));
			entity.setBlogUrl(cursor.getString(cursor.getColumnIndex(BlogListTable.BlogUrl)));
			entity.SetCateId(cursor.getInt(cursor.getColumnIndex(BlogListTable.CateId)));
			entity.SetCateName(cursor.getString(cursor.getColumnIndex(BlogListTable.CateName)));
			entity.SetCommentNum(cursor.getInt(cursor.getColumnIndex(BlogListTable.Comments)));
			entity.SetDiggsNum(cursor.getInt(cursor.getColumnIndex(BlogListTable.Digg)));
			entity.setIsFullText(cursor.getString(cursor.getColumnIndex(BlogListTable.IsFull)));
			entity.SetSummary(cursor.getString(cursor.getColumnIndex(BlogListTable.Summary)));
			entity.setUpdateTime(cursor.getString(cursor.getColumnIndex(BlogListTable.Updated)));
			entity.SetViewNum(cursor.getInt(cursor.getColumnIndex(BlogListTable.View)));
			entity.setIsReaded(cursor.getString(cursor.getColumnIndex(BlogListTable.IsReaded)));
			entity.SetUserName(cursor.getString(cursor.getColumnIndex(BlogListTable.UserName)));

			listBlog.add(entity);
		}

        if (cursor != null)
		    cursor.close();

		return listBlog;
	}

	public boolean getIsReaded(int blogId) {
		Blog entity = getBlogEntity(blogId);
		if (entity != null) {
			return entity.GetIsReaded();
		}
		return false;
	}

	public static void markAsReaded(int blogId) {
		String sql = "update BlogList set IsReaded=1 where BlogId=?";
		String[] args = {String.valueOf(blogId)};
		getRsd().execSQL(sql, args);
	}

	public static void synchronyContent2DB(int blogId, String blogContent) {
		if (TextUtils.isEmpty(blogContent)) {
			return;
		}
		String sql = "update BlogList set Content=?,isFull=1 where BlogId=?";
		String[] args = {blogContent, String.valueOf(blogId)};
		getWsd().execSQL(sql, args);
	}

	public static void synchronyData2DB(List<Blog> blogList) {
		List<ContentValues> list = new ArrayList<ContentValues>();

		for (int i = 0, len = blogList.size(); i < len; i++) {
            Blog blog = blogList.get(i);

			ContentValues contentValues = new ContentValues();
			contentValues.put(BlogListTable.BlogId, blog.GetBlogId());
			contentValues.put(BlogListTable.BlogTitle, blog.GetBlogTitle());
			contentValues.put(BlogListTable.Summary, blog.GetSummary());
			contentValues.put(BlogListTable.Content, blog.GetBlogContent());
			contentValues.put(BlogListTable.Published,
					TimeTools.parseDateToString(blog.GetAddTime()));
			contentValues.put(BlogListTable.Updated, TimeTools.parseDateToString(blog.GetUpdateTime()));
			contentValues.put(BlogListTable.AuthorName, blog.GetAuthor());
			contentValues.put(BlogListTable.AuthorAvatar, blog.GetAvator());
			contentValues.put(BlogListTable.AuthorUrl, blog.GetAuthorUrl());
			contentValues.put(BlogListTable.View, blog.GetViewNum());
			contentValues.put(BlogListTable.Comments, blog.GetCommentNum());
			contentValues.put(BlogListTable.Digg, blog.GetDiggsNum());
			contentValues.put(BlogListTable.IsReaded, false);
			contentValues.put(BlogListTable.CateId, blog.GetCateId());
			contentValues.put(BlogListTable.CateName, blog.GetCateName());
			contentValues.put(BlogListTable.IsFull, blog.GetIsFullText());
			contentValues.put(BlogListTable.BlogUrl, blog.GetBlogUrl());
			contentValues.put(BlogListTable.UserName, blog.GetUserName());

			list.add(contentValues);
		}

        getWsd().beginTransaction();
        try {
            for (ContentValues values : list) {
                int blogId = values.getAsInteger(BlogListTable.BlogId);
                boolean isExist = isExist(blogId);
                boolean isFull = isFull(blogId);

                if (!isExist) {
                    getWsd().insert(BlogListTable.TABLE_NAME, null, values);
                } else if (!isFull) {
                    synchronyContent2DB(values.getAsInteger(BlogListTable.BlogId),
                            values.getAsString(BlogListTable.Content));
                }
            }
            getWsd().setTransactionSuccessful();
        } finally {
            getWsd().endTransaction();
        }
	}
}
