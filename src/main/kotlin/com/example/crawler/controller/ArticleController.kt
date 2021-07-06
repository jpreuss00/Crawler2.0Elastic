package com.example.crawler.controller

import com.example.crawler.server.api.CrawlerApi
import com.example.crawler.server.model.Article
import com.example.crawler.service.ArticleService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "ArticleController", description = "Restful APIs related to articles")
@RestController
class ArticleController(@Autowired val articleService: ArticleService) : CrawlerApi {
    override fun addArticle(@RequestBody article: Article): ResponseEntity<Article>? = ResponseEntity(articleService.storeArticle(article), HttpStatus.OK)
    override fun getArticles(): ResponseEntity<List<Article>>? = ResponseEntity(articleService.getArticles(), HttpStatus.OK)
    override fun getArticlesByCategory(@RequestParam category: String): ResponseEntity<List<Article>>? = ResponseEntity(articleService.getArticlesByCategory(category), HttpStatus.OK)
    override fun getArticlesByTerm(@RequestParam term: String): ResponseEntity<List<Article>>? = ResponseEntity(articleService.getArticlesByTerm(term), HttpStatus.OK)
}
