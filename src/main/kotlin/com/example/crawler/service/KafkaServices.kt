package com.example.crawler.service

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.Items
import com.example.crawler.repository.KafkaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class KafkaServices(@Autowired val kafkaRepository: KafkaRepository) {

    fun writeArticlesInKafka(items: Items) {
        items.channel.item.forEach {
            kafkaRepository.writeInKafka(
                ArticleModel(
                    it.guid,
                    it.title,
                    it.category,
                    it.pubDate,
                    it.description,
                    it.link
                )
            )
        }
    }

}