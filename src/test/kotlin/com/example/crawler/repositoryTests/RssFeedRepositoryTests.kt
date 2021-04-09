package com.example.crawler.repositoryTests

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.Items
import com.example.crawler.repository.RssFeedRepository
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootTest
class RssFeedRepositoryTests {

    @Autowired
    private lateinit var rssfeedRepository: RssFeedRepository

    val wmServer = WireMockServer(8089)

    @Test
    fun readRssFeedShouldParseRssFeed() {
        val captionJsonString = String(Files.readAllBytes(Paths.get("src/test/resources/RssFeed.xml")))
        wmServer.start()
        wmServer.stubFor(
            any(urlEqualTo("/test"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody(captionJsonString)
            ))
        val items: Items = rssfeedRepository.readRssFeed(URL("http://localhost:8089/test"))
        wmServer.stop()

        var actual: List<ArticleModel> = items.channel.item
        val articleList = listOf(
            ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
            ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Wirtschaft", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
        )

        assertEquals(30, actual.size)
        assertEquals(articleList[0], actual[0])
        assertEquals(articleList[1], actual[1])
    }
}
