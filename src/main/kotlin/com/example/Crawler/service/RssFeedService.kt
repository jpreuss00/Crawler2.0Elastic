package com.example.crawler.service

import com.example.crawler.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RssFeedService(@Autowired val rssFeedRepository: RssFeedRepository, val articleRepository: ArticleRepository) {
    fun saveRssFeedToDB(section: String) {
        var items: Items = rssFeedRepository.readRssFeed("https://www.welt.de/feeds/$section.rss")
        items.channel.item.forEach{
            articleRepository.save(ArticleModel(it.guid, it.title, it.category, it.pubDate, it.description, it.link))
        }
    }
}
