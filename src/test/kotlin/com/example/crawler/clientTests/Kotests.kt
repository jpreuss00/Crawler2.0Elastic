package com.example.crawler.clientTests

import com.example.crawler.CrawlerApplication
import com.example.crawler.client.api.DefaultApi
import com.example.crawler.client.model.Article
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perTest
import junit.framework.Assert.assertEquals
import org.apache.http.HttpHost
import org.elasticsearch.client.Request
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource
import io.kotest.extensions.spring.SpringExtension
import junit.framework.Assert.assertNotNull
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.utility.DockerImageName
import java.util.function.Consumer

@TestPropertySource(properties = ["app.scheduling.enable=false"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [CrawlerApplication::class])
class Kotests : BehaviorSpec (){

    override fun extensions() = listOf(SpringExtension)

    @LocalServerPort
    val clientPort = 0

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

    init{

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

        var container = ElasticsearchContainer(
            DockerImageName
                .parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
                .withTag("7.10.1")
        )
            .withExposedPorts(containerExposedPort)
            .withCreateContainerCmdModifier(cmd)

        listener(container.perTest())

        container.start()
        var httpPort = container.firstMappedPort
        var restClientbuilder = RestClient.builder(HttpHost("localhost", httpPort, "http"))
        var client = RestHighLevelClient(restClientbuilder)
        var request = Request("PUT", "/articles")
        client.lowLevelClient.performRequest(request)
        container.stop()

        var api = DefaultApi()

        given("a started application with a database: getArticles") {

            api.apiClient.basePath = "http://localhost:$clientPort"

            var article = buildArticle()

            var expected = article

            `when`("the api write articles in the db and all articles are requested") {

                var response = api.addArticle(article)

                var result = api.articles

                assertNotNull(result)
                var actual = result[0]

                then("a correct response is returned and it sgould match the ones that were wrote befre") {

                    assertEquals(expected, response)
                    assertEquals(expected, actual)
                }
            }
        }

        given("a started application with a database: getArticlesByCategory") {

            api.apiClient.basePath = "http://localhost:$clientPort"

            `when`("the api write articles in the db") {

                var article = buildArticle()
                var article2 = buildArticle(229483808, category = "Gründerszene")

                api.addArticle(article)
                api.addArticle(article2)

                then("the db is searched for the articles with the given category") {

                    val category = "Gründerszene"

                    var expected = article2
                    var actual = api.getArticlesByCategory(category)[0]

                    assertEquals(expected, actual)

                }
            }
        }

        given("a started application with a database: getArticlesByTerm") {

            api.apiClient.basePath = "http://localhost:$clientPort"

            `when`("the api write articles in the db") {

                var article = buildArticle(229483806)
                var article2 = buildArticle(title = "Der überraschende Aufschwung der einstigen Lockdown-Gewinner", category = "Gründerszene")
                var article3 = buildArticle(229483808, category = "Gründerszene")

                api.addArticle(article)
                api.addArticle(article2)
                api.addArticle(article3)

                then("the db is searched for the articles with the given term") {

                    val term = "Verlierer"

                    var expected = listOf(article, article3)
                    var actual = api.getArticlesByTerm(term)

                    assertEquals(expected, actual)

                }
            }
        }
    }
}
