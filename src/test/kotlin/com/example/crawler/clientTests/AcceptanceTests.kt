package com.example.crawler.clientTests

import com.example.crawler.client.api.DefaultApi
import com.example.crawler.client.model.Article
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["app.scheduling.enable=false", "app.config.enable=true"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AcceptanceTests {

    @LocalServerPort
    private val clientPort = 0

    var api = DefaultApi()

    fun buildArticle(
        guid: Int? = 229483807,
        title: String? = "Der überraschende Aufschwung der einstigen Lockdown-Verlierer",
        category: String? = "Wirtschaft",
        description: String? = "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson."
    ): Article {
        var article = Article()
        article.guid = guid
        article.title = title
        article.category = category
        article.pubDate = "Wed, 31 Mar 2021 11:08:31 GMT"
        article.description = description
        article.link = "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"

        return article
    }

    @Test
    fun getArticlesShouldReturnArticle(){

        api.apiClient.basePath = "http://localhost:$clientPort"

        var article = buildArticle()

        api.addArticle(article)

        var result = api.articles

        assertNotNull(result)
        assertEquals(1, result.size)

        var expected = article
        var actual = result[0]

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesByCategoryShouldReturnArticle(){

        api.apiClient.basePath = "http://localhost:$clientPort"

        var article = buildArticle()
        var article2 = buildArticle(229483808, category = "Gründerszene")

        api.addArticle(article)
        api.addArticle(article2)

        val category = "Gründerszene"

        var result = api.getArticlesByCategory(category)

        assertNotNull(result)
        assertEquals(1, result.size)

        var expected = article2
        var actual = result[0]

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesByTermShouldReturnArticle(){

        api.apiClient.basePath = "http://localhost:$clientPort"

        var article = buildArticle(229483806)
        var article2 = buildArticle(title = "Der überraschende Aufschwung der einstigen Lockdown-Gewinner", category = "Gründerszene")
        var article3 = buildArticle(229483808, category = "Gründerszene")

        api.addArticle(article)
        api.addArticle(article2)
        api.addArticle(article3)

        val term = "Verlierer"

        var result = api.getArticlesByTerm(term)

        assertNotNull(result)
        assertEquals(2, result.size)

        var expected = listOf(article, article3)
        var actual = result

        assertEquals(expected, actual)
    }
}
