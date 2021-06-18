package com.example.crawler.service

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import com.example.crawler.server.model.Article
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {

    fun convertToServerModel(articleModel: ArticleModel): Article{
        return Article()
            .guid(articleModel.guid.toInt())
            .title(articleModel.title)
            .category(articleModel.category)
            .pubDate(articleModel.pubDate)
            .description(articleModel.description)
            .link(articleModel.link)
    }

    fun convertToDBModel(article: Article): ArticleModel{
        return ArticleModel(article.guid.toLong(),
            article.title,
            article.category,
            article.pubDate,
            article.description,
            article.link)
    }

    fun storeArticle(article: Article): Article = convertToServerModel(articleRepository.save(convertToDBModel(article)))
    fun storeArticles(articles: List<ArticleModel>): List<ArticleModel> = articleRepository.saveAll(articles).toList()
    fun getArticles(): List<Article> = articleRepository.findAll().map { convertToServerModel(it) }
    fun getArticlesByCategory(category: String): List<Article> = articleRepository.findByCategoryContaining(category).map { convertToServerModel(it) }
    fun getArticlesByTerm(term: String): List<Article> = articleRepository.findByDescriptionContainingOrTitleContaining(term, term).map { convertToServerModel(it) }
}
