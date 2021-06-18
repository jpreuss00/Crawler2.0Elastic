package com.example.crawler

import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration

@ConditionalOnProperty(
    value = ["app.config.enable"], havingValue = "true", matchIfMissing = false
)
@Configuration
class ElasticSearchTestConfiguration : AbstractElasticsearchConfiguration() {

    @Bean
    override fun elasticsearchClient(): RestHighLevelClient {
        val clientConfiguration = ClientConfiguration.builder()
            .connectedTo("localhost:9200")
            .build()
        return RestClients.create(clientConfiguration).rest()
    }

    @Bean(destroyMethod = "shutdown")
    fun embeddedElastic() = EmbeddedElastic()
}
