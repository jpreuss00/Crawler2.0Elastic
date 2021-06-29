package com.example.crawler.controller

import com.example.crawler.kafka.KafkaConsumerConfig
import com.example.crawler.kafka.KafkaProducerConfig
import com.example.crawler.server.api.CrawlerApi
import com.example.crawler.server.model.Article
import com.example.crawler.service.ArticleService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiParam

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Api(value = "ArticleController", description = "Restful APIs related to articles")
@RestController
class ArticleController(@Autowired val articleService: ArticleService) : CrawlerApi {

    @Autowired
    val kafkaProducerConfig: KafkaProducerConfig = KafkaProducerConfig()

    @Autowired
    val kafkaConsumerConfig: KafkaConsumerConfig = KafkaConsumerConfig()

    override fun addArticle(@RequestBody article: Article): ResponseEntity<Article>?  {
        return ResponseEntity(articleService.storeArticle(article), HttpStatus.OK)
    }

    override fun getArticles(): ResponseEntity<List<Article>>? {
        kafkaProducerConfig.sendMessage("articles have been requested")
        kafkaConsumerConfig.listenToArticleTopic("articles have been requested")
        return ResponseEntity(articleService.getArticles(), HttpStatus.OK)
    }

    override fun getArticlesByCategory(@RequestParam category: String): ResponseEntity<List<Article>>? {
        return ResponseEntity(articleService.getArticlesByCategory(category), HttpStatus.OK)
    }

    override fun getArticlesByTerm(@RequestParam term: String): ResponseEntity<List<Article>>? {
        return ResponseEntity(articleService.getArticlesByTerm(term), HttpStatus.OK)
    }
}
