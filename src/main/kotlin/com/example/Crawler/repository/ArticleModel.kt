package com.example.crawler.repository

import javax.persistence.*

@Entity
@Table(name="Articles")
data class ArticleModel(

    @Id
    @GeneratedValue
    val id: Long,

    val guid: Long,
    val title: String,
    val category: String,
    val pubDate: String,
    val description: String,
    val link: String
)
