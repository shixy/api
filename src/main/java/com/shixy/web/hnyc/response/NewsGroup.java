package com.shixy.web.hnyc.response;

import java.util.List;

public class NewsGroup {

	private String groupName;
	private List<News> news;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

}
