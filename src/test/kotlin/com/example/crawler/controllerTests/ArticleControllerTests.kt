package com.example.crawler.controllerTests

import com.example.crawler.controller.ArticleController
import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import junit.framework.Assert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.Ports
import org.junit.AfterClass
import org.junit.jupiter.api.*
import org.springframework.test.annotation.DirtiesContext
import java.util.function.Consumer

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ArticleControllerTests {

    var hostPort = 9200
    var containerExposedPort = 9200
    var cmd: Consumer<CreateContainerCmd> =
        Consumer<CreateContainerCmd> { e ->
            e.withPortBindings(
                PortBinding(
                    Ports.Binding.bindPort(hostPort),
                    ExposedPort(containerExposedPort)
                )
            )
        }

    @Container
    var container = ElasticsearchContainer(
        DockerImageName
            .parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
            .withTag("7.10.1")
    )
        .withExposedPorts(containerExposedPort)
        .withCreateContainerCmdModifier(cmd)

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Autowired
    private lateinit var articleController: ArticleController

    val articleList = listOf(
        ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html"),
        ArticleModel(229346251, "„Sicherste Standorte“ – und trotzdem dürfen die Deutschen nicht in die Baumärkte", "Gründerszene", "Wed, 31 Mar 2021 11:08:13 GMT", "Die Gartensaison beginnt – und normalerweise ziehen die Deutschen los, um sich mit Pflanzen und Erde einzudecken. Doch nach dem Rekordjahr 2020 sind die Baumärkte nun vielerorts dicht. Und sobald die Märkte wieder öffnen dürfen, droht bereits der erste Engpass.", "https://www.welt.de/wirtschaft/article229346251/Start-der-Gartensaison-und-Corona-Jetzt-leiden-auch-Baumaerkte.html"),
    )

    @BeforeEach
    fun setUp(){
        container.start()
        articleRepository.saveAll(articleList)
    }

    @AfterEach
    fun tearDown(){
        container.stop()
    }

    @Test
    fun addArticleShouldAddAnArticle() {

        val article = ArticleModel(229483807, "Der überraschende Aufschwung der einstigen Lockdown-Verlierer", "Wirtschaft", "Wed, 31 Mar 2021 11:08:31 GMT", "Die Briten haben den dritten Lockdown erstaunlich gut verkraftet. Aufgrund der großen Impffortschritte hofft das Land nun darauf, dass kein weiteres Herunterfahren mehr nötig ist. Doch der Vakzin-Streit mit Brüssel gefährdet das ehrgeizige Ziel von Premier Boris Johnson.", "https://www.welt.de/wirtschaft/article229483807/Grossbritannien-Ueberraschender-Aufschwung-der-Lockdown-Verlierer.html")

        val actual = articleController.addArticle(article)
        val expected = article

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesShouldReturnAllGivenArticles() {

        val actual = articleController.getArticles()
        val expected = articleList

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesByCategoryShouldReturnGivenArticles() {

        val actual = articleController.getArticlesByCategory("Gründerszene")
        val expected = listOf(articleList[1])

        assertEquals(expected, actual)
    }

    @Test
    fun getArticlesByTermShouldReturnGivenArticles() {

        val actual = articleController.getArticlesByTerm("Aufschwung")
        val expected = listOf(articleList[0])

        assertEquals(expected, actual)
    }
}
