package com.example.crawler.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import javax.persistence.*

@Entity
@Table(name = "Articles")
data class ArticleModel(

    @Id
    @set:JsonProperty("guid")
    var guid: Long,

    @Column(length = 65535)
    @set:JsonProperty("title")
    var title: String,

    @set:JsonProperty("category")
    var category: String,

    @set:JsonProperty("pubDate")
    var pubDate: String,

    @Column(length = 65535)
    @set:JsonProperty("description")
    var description: String,

    @Column(length = 65535)
    @set:JsonProperty("link")
    var link: String,
)

@JsonRootName("item")
data class Item(
    @set:JsonProperty("item")
    var item: List<ArticleModel> = ArrayList()
)

@JsonRootName("channel")
data class Items(
    @set:JsonProperty("channel")
    var channel: Item
)
