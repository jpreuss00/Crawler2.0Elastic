package com.example.crawler.repositoryTests

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.util.function.Consumer

@SpringBootTest
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ArticleRepositoryTests {

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

    val articleList = listOf(
        ArticleModel(1, "a title", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "This is a description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
        ArticleModel(2, "another header", "Karriere", "Thu, 01 Apr 2021 09:28:47 GMT", "I want to", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
        ArticleModel(3, "a title", "Wirtschaft", "Thu, 01 Apr 2021 09:28:47 GMT", "This is another description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
        ArticleModel(4, "exclusive headline", "Geschichte", "Thu, 01 Apr 2021 09:28:47 GMT", "This is the body part", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
        ArticleModel(5, "this is a title description", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "break free", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html")
    )

    @Autowired
    private lateinit var articleRepository: ArticleRepository

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
    fun findByCategoryShouldReturnGivenArticles() {

        val category = "Gründerszene"

        articleRepository.saveAll(articleList)
        val actual: List<ArticleModel> = articleRepository.findByCategoryContaining(category)

        assertEquals(2, actual.count())
        assertEquals(articleList[0], actual[0])
        assertEquals(articleList[4], actual[1])
    }

    @Test
    fun findByTermShouldReturnGivenArticles() {

        val term = "description"

        articleRepository.saveAll(articleList)
        val actual: List<ArticleModel> = articleRepository.findByDescriptionContainingOrTitleContaining(term, term)

        assertEquals(3, actual.count())
        assertEquals(articleList[0], actual[0])
        assertEquals(articleList[2], actual[1])
        assertEquals(articleList[4], actual[2])
    }
}