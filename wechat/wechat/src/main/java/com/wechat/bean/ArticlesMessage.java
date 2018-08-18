package com.wechat.bean;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class ArticlesMessage extends BaseMessage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XStreamAlias("ArticleCount")
	private int ArticleCount;

	@XStreamAlias("Articles")
	private List<ArticlesItem> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<ArticlesItem> getArticles() {
		return Articles;
	}

	public void setArticles(List<ArticlesItem> articles) {
		Articles = articles;
	}
}