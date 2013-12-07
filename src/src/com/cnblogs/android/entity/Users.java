package com.cnblogs.android.entity;

import java.util.Date;

public class Users {
	private int _userId;
	private String _userName;
	private String _blogName;
	private String _avator;
	private String _blogUrl;
	private int _blogCount;
	private Date _blogBirth;
	private Date _lastUpdate;
	public void SetUserId(int _userId) {
		this._userId = _userId;
	}
	public int GetUserId() {
		return _userId;
	}
	public void SetUserName(String _userName) {
		this._userName = _userName;
	}
	public String GetUserName() {
		return _userName;
	}
	public void SetAvator(String _avator) {
		this._avator = _avator;
	}
	public String GetAvator() {
		return _avator;
	}

	public void SetBlogUrl(String _blogUrl) {
		this._blogUrl = _blogUrl;
	}

	public String GetBlogUrl() {
		return _blogUrl;
	}

	public void SetBlogCount(int _blogCount) {
		this._blogCount = _blogCount;
	}

	public int GetBlogCount() {
		return _blogCount;
	}

	public void SetBlogBirth(Date _blogBirth) {
		this._blogBirth = _blogBirth;
	}

	public Date GetBlogBirth() {
		return _blogBirth;
	}
	
	public void SetBlogName(String _blogName) {
		this._blogName = _blogName;
	}
	
	public String GetBlogName() {
		return _blogName;
	}
	
	public void SetLastUpdate(Date _lastUpdate) {
		this._lastUpdate = _lastUpdate;
	}
	
	public Date GetLastUpdate() {
		return _lastUpdate;
	}
}
