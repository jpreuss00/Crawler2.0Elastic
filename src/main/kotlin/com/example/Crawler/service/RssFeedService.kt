package com.example.crawler.service

import com.example.crawler.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RssFeedService(@Autowired val rssFeedRepository: RssFeedRepository, @Autowired val articleService: ArticleService) {
    fun readRssFeed(section: String) = rssFeedRepository.readRssFeed("https://www.welt.de/feeds/$section.rss")
    fun saveRssFeedToDB(items: Items) = articleService.storeArticles(items.channel.item.map {  it -> ArticleModel(it.guid, it.title, it.category, it.pubDate, it.description, it.link) })
}
