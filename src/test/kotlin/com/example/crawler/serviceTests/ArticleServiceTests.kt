package com.example.crawler.serviceTests

import com.example.crawler.assertArticles
import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import com.example.crawler.server.model.Article
import com.example.crawler.service.ArticleService
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ArticleServiceTests {

    val articleList = listOf(
        ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
        ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Gründerszene", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
    )

    var article1: Article = Article()
        .guid(229483807)
        .title("Der überraschende Aufschwung der einstigen Lockdown-Verlierer")
        .category("Wirtschaft")
        .pubDate("Wed, 31 Mar 2021 11:08:31 GMT")
        .description("Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.")
        .link("https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html")

    var article2: Article = Article()
        .guid(229346251)
        .title("„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte")
        .category("Gründerszene")
        .pubDate("Wed, 31 Mar 2021 11:08:13 GMT")
        .description("Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.")
        .link("https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html")

    val articleServerList = listOf(article1, article2)

    val articleRepositoryMock = Mockito.mock(ArticleRepository::class.java)
    val articleService = ArticleService(articleRepositoryMock)

    @Test
    fun storeArticleShouldStoreOneArticle() {

        Mockito.`when`(articleRepositoryMock.save(
            articleList[0]
        )).thenReturn(
            articleList[0]
        )

        val actual = articleService.storeArticle(articleServerList[0])
        val expected = articleList[0]

        assertArticles(expected, actual)
    }

    @Test
    fun storeArticlesShouldStoreMultipleArticles() {

        Mockito.`when`(articleRepositoryMock.saveAll(
            articleList
        )).thenReturn(
            articleList
        )

        val actual = articleService.storeArticles(articleList)
        val expected = articleList

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesShouldReturnAllGivenArticles() {

        Mockito.`when`(articleRepositoryMock.findAll()).thenReturn(
            articleList
        )

        val actual = articleService.getArticles()
        val expected = articleList

        assertArticles(expected[0], actual[0])
        assertArticles(expected[1], actual[1])
    }

    @Test
    fun getArticlesByCategoryShouldReturnGivenArticles() {

        Mockito.`when`(articleRepositoryMock.findByCategoryContaining(
            "Gründerszene"
        )).thenReturn(
            listOf(articleList[1])
        )

        val actual = articleService.getArticlesByCategory("Gründerszene")
        val expected = listOf(articleList[1])

        assertArticles(expected[0], actual[0])
    }

    @Test
    fun getArticlesByTermShouldReturnGivenArticles() {

        val term = "Aufschwung"

        Mockito.`when`(articleRepositoryMock.findByDescriptionContainingOrTitleContaining(
            term, term
        )).thenReturn(
            listOf(articleList[0])
        )

        val actual = articleService.getArticlesByTerm(term)
        val expected = listOf(articleList[0])

        assertArticles(expected[0], actual[0])
    }
}
