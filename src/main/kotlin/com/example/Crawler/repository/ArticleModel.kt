package com.example.crawler.repository

import javax.persistence.*

@Entity
@Table(name = "Articles")
data class ArticleModel(

    val guid: Long,
    @Column(length = 65535)
    val title: String,
    val category: String,
    val pubDate: String,
    @Column(length = 65535)
    val description: String,
    @Column(length = 65535)
    val link: String,

    @Id
    @GeneratedValue
    val id: Long? = null
)
