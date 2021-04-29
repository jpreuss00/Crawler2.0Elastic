package com.example.crawler.repository

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : ElasticsearchRepository<ArticleModel, Long> {
    fun findByCategoryContaining(category: String): List<ArticleModel>
    fun findByDescriptionContainingOrTitleContaining(description: String, title: String): List<ArticleModel>
    override fun findAll(): List<ArticleModel>
}