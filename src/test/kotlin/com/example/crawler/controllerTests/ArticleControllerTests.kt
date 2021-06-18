package com.example.crawler.controllerTests

import com.example.crawler.assertArticles
import com.example.crawler.controller.ArticleController
import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import com.example.crawler.server.model.Article
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["app.config.enable=true"])
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ArticleControllerTests {

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var articleController: ArticleController

    val articleList = listOf(
        ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
        ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Gründerszene", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
    )

    @BeforeEach
    fun deleteAll(){
        articleRepository.saveAll(articleList)
    }

    @Test
    fun addArticleShouldAddAnArticle() {

        val article = Article()
            .guid(229483807)
            .title("Der überraschende Aufschwung der einstigen Lockdown-Verlierer")
            .category("Wirtschaft")
            .pubDate("Wed, 31 Mar 2021 11:08:31 GMT")
            .category("Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.")
            .link("https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html")

        val actual = articleController.addArticle(article)
        val expected = article

        assertEquals(expected, actual?.body)
    }

    @Test
    fun getArticlesShouldReturnAllGivenArticles() {

        val actual = articleController.getArticles()
        val expected = articleList

        assertNotNull(actual)
        assertNotNull(actual!!.body)
        assertEquals(2 ,actual.body!!.size)

        assertArticles(expected[0], actual.body!![0])
        assertArticles(expected[1], actual.body!![1])
    }

    @Test
    fun getArticlesByCategoryShouldReturnGivenArticles() {

        val actual = articleController.getArticlesByCategory("Gründerszene")
        val expected = listOf(articleList[1])

        assertNotNull(actual)
        assertNotNull(actual!!.body)
        assertEquals(1 ,actual.body!!.size)

        assertArticles(expected[0], actual.body!![0])
    }

    @Test
    fun getArticlesByTermShouldReturnGivenArticles() {

        val actual = articleController.getArticlesByTerm("Aufschwung")
        val expected = listOf(articleList[0])

        assertNotNull(actual)
        assertNotNull(actual!!.body)
        assertEquals(1 ,actual.body!!.size)

        assertArticles(expected[0], actual.body!![0])
    }
}
