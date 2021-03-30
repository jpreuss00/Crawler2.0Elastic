package com.example.crawler.service

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {
    fun storeArticle(article: ArticleModel) = articleRepository.save(article)
    fun storeArticles(articles: List<ArticleModel>): MutableIterable<ArticleModel> = articleRepository.saveAll(articles)
    fun getArticles(): MutableIterable<ArticleModel> = articleRepository.findAll()
    fun getArticlesByCategory(category: String): MutableIterable<ArticleModel> = articleRepository.findByCategoryContaining(category)
    fun getArticlesByTerm(term: String): MutableIterable<ArticleModel> = articleRepository.findByDescriptionContainingOrTitleContaining(term, term)
}
