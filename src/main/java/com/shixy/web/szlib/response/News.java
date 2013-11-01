package com.shixy.web.szlib.response;

public class News {
	private String id;
	private String title;
	private String publishDate;
	
	public News(String id,String title, String publishDate) {
		this.id = id;
		this.title = title;
		this.publishDate = publishDate;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	
	
}
