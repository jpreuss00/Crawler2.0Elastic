package com.example.crawler.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<ArticleModel, Long> {
    fun findByCategoryContaining(category: String): List<ArticleModel>
    fun findByDescriptionContainingOrTitleContaining(description: String, title: String): List<ArticleModel>
}
