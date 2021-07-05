package com.example.crawler.kafka

import com.example.crawler.repository.ArticleModel
import com.example.crawler.utils.ArticleDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.annotation.KafkaListener

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String = "localhost:9092"

    @Bean
    fun consumerFactory(): ConsumerFactory<String, ArticleModel> {
        val props: MutableMap<String, Any?> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = "welt"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = ArticleDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ArticleModel> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, ArticleModel>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    @KafkaListener(
        topics = ["\${message.topic.name}"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listenToArticleTopic(message: ConsumerRecord<String, ArticleModel>) {
        println(message)
        println(message.value())
    }
}
