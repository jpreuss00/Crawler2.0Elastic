package com.example.crawler.utils

import com.example.crawler.repository.ArticleModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import java.lang.Exception

class ArticleSerializer : Serializer<ArticleModel> {

    override fun configure(var1: Map<String?, *>?, b: Boolean) {}

    override fun serialize(var1: String?, var2: ArticleModel?): ByteArray? {
        var retVal: ByteArray? = null
        val objectMapper = ObjectMapper()
        try {
            retVal = objectMapper.writeValueAsString(var2).toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return retVal
    }

    override fun close() {}
}