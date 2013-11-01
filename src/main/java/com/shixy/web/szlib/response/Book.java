package com.shixy.web.szlib.response;

import java.util.Set;

public class Book {
	
	private String recordNo;//深图记录ID
	private String title;//书名
	private String author;//作者
	private String publisher;//出版社
	private String publishDate;//出版日期
	private String description;//简介
	private boolean canReverse;//是否可以预借
	private String catalog;//目录
	private String price;//价格
	private String image;//封面
	private String isbn;//isbn
	private	String page;//总页数
	private String doubanId;//豆瓣书籍记录ID
	private int numRaters;//评价人数
	private String average;//书籍评分
	private Set<String> collectAddress;
	
	public Book() {
	}
	
	public Book(String recordNo,String title,String author,String publisher,String publishDate,String description,boolean canReverse) {
		this.recordNo = recordNo;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.publishDate = publishDate;
		this.description = description;
		this.canReverse = canReverse;
	}
	
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isCanReverse() {
		return canReverse;
	}
	public void setCanReverse(boolean canReverse) {
		this.canReverse = canReverse;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getDoubanId() {
		return doubanId;
	}

	public void setDoubanId(String doubanId) {
		this.doubanId = doubanId;
	}


	public int getNumRaters() {
		return numRaters;
	}

	public void setNumRaters(int numRaters) {
		this.numRaters = numRaters;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public Set<String> getCollectAddress() {
		return collectAddress;
	}

	public void setCollectAddress(Set<String> collectAddress) {
		this.collectAddress = collectAddress;
	}
	

}
