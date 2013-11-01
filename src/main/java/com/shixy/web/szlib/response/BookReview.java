package com.shixy.web.szlib.response;

public class BookReview {
	private String title;
	private String user;
	private String userIcon;
	private String publishDate;
	private String summary;
	private String url;
	
	public BookReview(String title,String user,String userIcon,String publishDate,String summary,String url) {
		this.title = title;
		this.user = user;
		this.userIcon = userIcon;
		this.publishDate = publishDate;
		this.summary = summary;
		this.url = url;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
