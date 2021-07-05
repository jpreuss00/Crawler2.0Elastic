package com.example.crawler.utils

import com.example.crawler.repository.ArticleModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import java.lang.Exception

class ArticleDeserializer : Deserializer<ArticleModel> {

    override fun close() {}

    override fun configure(var1: Map<String?, *>?, var2: Boolean) {}

    override fun deserialize(var1: String?, var2: ByteArray?): ArticleModel? {
        val mapper = ObjectMapper()
        var articleModel: ArticleModel? = null
        try {
            articleModel = mapper.readValue(var2, ArticleModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return articleModel
    }
}