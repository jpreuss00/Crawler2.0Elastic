package com.example.crawler.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName

data class JacksonArticle(
    @set:JsonProperty("guid")
    var guid: Long,

    @set:JsonProperty("title")
    var title: String,

    @set:JsonProperty("category")
    var category: String,

    @set:JsonProperty("pubDate")
    var pubDate: String,

    @set:JsonProperty("description")
    var description: String,

    @set:JsonProperty("link")
    var link: String,
)

@JsonRootName("item")
data class Item(
    @set:JsonProperty("item")
    var item: List<JacksonArticle> = ArrayList()
)

@JsonRootName("channel")
data class Items(
    @set:JsonProperty("channel")
    var channel: Item
)