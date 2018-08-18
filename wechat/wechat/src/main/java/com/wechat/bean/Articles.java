package com.wechat.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Articles")
public class Articles {
	@XStreamAlias("ArticlesItem")
	private List<ArticlesItem> Articles;

	public List<ArticlesItem> getArticles() {
		return Articles;
	}

	public void setArticles(List<ArticlesItem> articles) {
		Articles = articles;
	}

}