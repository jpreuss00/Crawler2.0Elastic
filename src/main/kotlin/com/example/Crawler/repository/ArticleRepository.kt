package com.example.crawler.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : CrudRepository<ArticleModel, Long> {
    fun findByCategoryContaining(category: String): MutableIterable<ArticleModel>
    fun findByDescriptionContainingOrTitleContaining(description: String, title: String): MutableIterable<ArticleModel>
}
