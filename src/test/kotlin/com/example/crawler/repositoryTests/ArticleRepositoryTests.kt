package com.example.crawler.repositoryTests

import com.example.crawler.repository.ArticleModel
import com.example.crawler.repository.ArticleRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.streams.asSequence

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class ArticleRepositoryTests {

    companion object {
        @Container
        private val postgreSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest")

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgreSQLContainer::getUsername)
            registry.add("spring.datasource.password", postgreSQLContainer::getPassword)
        }
    }

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @Test
    fun findByCategoryShouldReturnGivenArticles() {
        val articleList = listOf(
            ArticleModel(1, "a title", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "This is a description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(2, "another header", "Karriere", "Thu, 01 Apr 2021 09:28:47 GMT", "I want to", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(3, "a title", "Wirtschaft", "Thu, 01 Apr 2021 09:28:47 GMT", "This is another description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(4, "exclusive headline", "Geschichte", "Thu, 01 Apr 2021 09:28:47 GMT", "This is the body part", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(5, "this is a title description", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "break free", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html")
        )
        val category = "Gründerszene"

        articleRepository.saveAll(articleList)
        val actual: List<ArticleModel> = articleRepository.findByCategoryContaining(category)

        assertEquals( 2, actual.count())
        assertEquals(articleList[0], actual[0])
        assertEquals(articleList[4], actual[1])
    }

    @Test
    fun findByTermShouldReturnGivenArticles() {
        val articleList = listOf(
            ArticleModel(1, "a title", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "This is a description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(2, "another header", "Karriere", "Thu, 01 Apr 2021 09:28:47 GMT", "I want to", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(3, "a title", "Wirtschaft", "Thu, 01 Apr 2021 09:28:47 GMT", "This is another description", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(4, "exclusive headline", "Geschichte", "Thu, 01 Apr 2021 09:28:47 GMT", "This is the body part", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html"),
            ArticleModel(5, "this is a title description", "Gründerszene", "Thu, 01 Apr 2021 09:28:47 GMT", "break free", "https://www.welt.de/vermischtes/article229542865/Joko-und-Klaas-Kapern-ProSieben-zeigen-den-Pflegealltag-in-Echtzeit.html")
        )
        val term = "description"

        articleRepository.saveAll(articleList)
        val actual: List<ArticleModel> = articleRepository.findByDescriptionContainingOrTitleContaining(term, term)

        assertEquals(3, actual.count())
        assertEquals(articleList[0], actual[0])
        assertEquals(articleList[2], actual[1])
        assertEquals(articleList[4], actual[2])
    }

    @Test
    fun store1MilArticles() {

        fun getRandomText(length: Int): String {
            val titleList = arrayOf("Ein", "ein", "Spiel", "Schnee", "Die", "die", "Dem", "dem", "Des", "des", "Das", "das", "Auto", "Marke", "Wort", "Text", "Team", "Schule", "Video", "Kamera", "Ei", "Bildschirm", "Kaffe", "Eis", "Film", "Von", "von", "Vom", "vom")
            var randomText = ""
            for ( h in 0..length){
                var randomText = titleList.random() + " "
            }
            return randomText
        }

        var guidGenerator: Long = 0
        for (i in 0..10 ){
            var articleArray: ArrayList<ArticleModel> = ArrayList()
            for ( j in 0..1000 ){
                articleArray.add(ArticleModel(guidGenerator, getRandomText(Random.nextInt(3, 7)), getRandomText(Random.nextInt(1,3)), LocalDateTime.now().toString(), getRandomText(Random.nextInt(50, 100)), "https://google.com/"))
                guidGenerator++
            }
            println("start saving... " + LocalTime.now())
            articleRepository.saveAll(articleArray)
            println("finish saving... " + LocalTime.now())
            articleArray.clear()
        }

        println("find articles starting... " + LocalTime.now())
        var actual = articleRepository.findByCategoryContaining("Spiel")
        println("find articles finishing... " + LocalTime.now())

        println("start asserting... " + LocalTime.now())
        actual.forEach{
            assertTrue(it.category.contains("Spiel"))
        }
        println("finish asserting... " + LocalTime.now())
    }
}
