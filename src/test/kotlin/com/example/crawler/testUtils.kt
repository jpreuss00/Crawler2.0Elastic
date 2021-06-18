package com.example.crawler

import com.example.crawler.repository.ArticleModel
import com.example.crawler.server.model.Article
import junit.framework.Assert.assertEquals

    fun assertArticles(articleModel: ArticleModel, article: Article){
        assertEquals(article.guid.toLong(), articleModel.guid)
        assertEquals(article.title, articleModel.title)
        assertEquals(article.category, articleModel.category)
        assertEquals(article.pubDate, articleModel.pubDate)
        assertEquals(article.description, articleModel.description)
        assertEquals(article.link, articleModel.link)
    }
