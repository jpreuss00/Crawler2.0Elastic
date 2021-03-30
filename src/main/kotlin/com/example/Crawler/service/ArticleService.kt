package com.example.crawler.service

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ArticleService(@Autowired val articleRepository: ArticleRepository) {

    fun storeArticle(article: ArticleModel){
        articleRepository.save(article)
    }

    fun getArticles(): MutableIterable<ArticleModel>{
        return articleRepository.findAll()
    }

    fun getArticlesByCategory(category: String): MutableIterable<ArticleModel> {
        return articleRepository.findByCategoryContaining(category)
    }

    fun getArticlesByTerm(description: String, title: String): MutableIterable<ArticleModel> {
        return articleRepository.findByDescriptionContainingOrTitleContaining(description, title)
    }

    fun deleteArticleById(id: Long){
        articleRepository.deleteById(id)
    }
}
