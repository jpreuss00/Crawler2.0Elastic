package com.example.crawler.service

import com.example.crawler.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URL
import com.example.crawler.repository.ArticleModel

@Service
class RssFeedService(@Autowired val rssFeedRepository: RssFeedRepository, @Autowired val articleService: ArticleService) {
    fun readRssFeed(section: String) = rssFeedRepository.readRssFeed(URL("https://www.welt.de/feeds/$section.rss"))
    fun saveRssFeedToDB(items: Items) = articleService.storeArticles(items.channel.item.map {  it -> ArticleModel(it.guid, it.title, it.category, it.pubDate, it.description, it.link) })
}
