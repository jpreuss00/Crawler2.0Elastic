package com.example.crawler.serviceTests

import com.example.crawler.repository.*
import com.example.crawler.service.ArticleService
import com.example.crawler.service.RssFeedService
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.net.URL
import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.Items
import com.example.crawler.repository.Item
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RssFeedServiceTests {

    val items = Items(Item(
        listOf(
            ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
            ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Gründerszene", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
        )
    ))

    val rssFeedRepositoryMock = Mockito.mock(RssFeedRepository::class.java)
    val articleServiceMock = Mockito.mock(ArticleService::class.java)
    val rssFeedService = RssFeedService(rssFeedRepositoryMock, articleServiceMock)

    @Test
    fun readRssFeedShouldReturnGivenFeed() {

        Mockito.`when`(rssFeedRepositoryMock.readRssFeed(
            URL("https://www.welt.de/feeds/latest.rss")
        )).thenReturn(
            items
        )

        val actual = rssFeedService.readRssFeed("latest")
        val expected = items

        assertEquals(expected, actual)
    }

    @Test
    fun saveRssFeedToDBShouldSaveFeed() {

        val articleList = listOf(
            ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
            ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Gründerszene", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
        )

        Mockito.`when`(articleServiceMock.storeArticles(
            items.channel.item.map { ArticleModel(it.guid, it.title, it.category, it.pubDate, it.description, it.link) }
        )).thenReturn(
            articleList
        )

        val actual = rssFeedService.saveRssFeedToDB(items)
        val expected = articleList

        assertEquals(expected, actual)
    }
}
