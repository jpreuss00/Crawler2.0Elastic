package com.example.crawler.kafka

import com.example.crawler.repository.ArticleModel
import com.example.crawler.utils.ArticleSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@EnableKafka
@Configuration
class KafkaProducerConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String = "localhost:9092"

    @Bean
    fun producerFactory(): ProducerFactory<String, ArticleModel> {
        val configProps: MutableMap<String, Any?> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = ArticleSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, ArticleModel> {
        return KafkaTemplate(producerFactory())
    }

    @Value(value = "\${message.topic.name}")
    private val topicName: String = "article"

    fun sendMessage(articleModel: ArticleModel) {
        kafkaTemplate().send(topicName, articleModel)
    }
}
