package com.example.crawler.repository

import com.example.crawler.kafka.KafkaConsumerConfig
import com.example.crawler.kafka.KafkaProducerConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class KafkaRepository(@Autowired val kafkaProducerConfig: KafkaProducerConfig, @Autowired val kafkaConsumerConfig: KafkaConsumerConfig) {

    fun writeInKafka(articleModel: ArticleModel) {
        kafkaProducerConfig.sendMessage(articleModel)
    }

}