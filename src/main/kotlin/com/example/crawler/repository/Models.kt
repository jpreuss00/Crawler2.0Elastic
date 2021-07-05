package com.example.crawler.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "articles", createIndex = true)
data class ArticleModel(

    @Id
    @JsonProperty("guid")
    var guid: Long,

    @JsonProperty("title")
    var title: String?,

    @JsonProperty("category")
    var category: String?,

    @JsonProperty("pubDate")
    var pubDate: String?,

    @JsonProperty("description")
    var description: String?,

    @JsonProperty("link")
    var link: String?,
)

@JsonRootName("item")
data class Item(
    @JsonProperty("item")
    var item: List<ArticleModel> = ArrayList()
)

@JsonRootName("channel")
data class Items(
    @JsonProperty("channel")
    var channel: Item
)
