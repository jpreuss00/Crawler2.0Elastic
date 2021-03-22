package com.example.crawler

import com.example.crawler.controller.ArticleController
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrawlerApplication

fun main(args: Array<String>) {
	runApplication<CrawlerApplication>(*args)
	ArticleController()
}
