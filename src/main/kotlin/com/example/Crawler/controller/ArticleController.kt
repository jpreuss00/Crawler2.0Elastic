package com.example.crawler.controller

import com.example.crawler.repository.ArticleModel
import com.example.crawler.service.ArticleService
import com.example.crawler.service.RssFeedService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ArticleController(@Autowired val articleService: ArticleService, val rssFeedService: RssFeedService) {
   @PostMapping("/addArticle")
    fun addArticle(@RequestBody article: ArticleModel) {
        articleService.storeArticle(article)
    }

    @GetMapping("/getArticles")
    fun getArticles(): MutableIterable<ArticleModel> {
        return articleService.getArticles()
    }

    @GetMapping("/getArticlesByCategory")
    fun getArticlesByCategory(@RequestParam category: String): MutableIterable<ArticleModel> {
        return articleService.getArticlesByCategory(category)
    }

    @GetMapping("/getArticlesByTerm")
    fun getArticlesByTerm(@RequestParam term: String): MutableIterable<ArticleModel> {
        return articleService.getArticlesByTerm(term, term)
    }

    @DeleteMapping("/deleteArticle")
    fun deleteArticle(@RequestParam id: Long) {
        articleService.deleteArticleById(id)
    }
}
