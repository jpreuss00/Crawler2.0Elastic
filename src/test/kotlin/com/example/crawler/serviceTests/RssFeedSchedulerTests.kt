package com.example.crawler.serviceTests

import com.example.crawler.service.KafkaServices
import com.example.crawler.service.RssFeedScheduler
import com.example.crawler.service.RssFeedService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RssFeedSchedulerTests {

    @Test
    fun scheduleRSSFeedShouldSaveTheNewestArticles() {
        val rssFeedServiceMock = Mockito.mock(RssFeedService::class.java)
        val kafkaServicesMock = Mockito.mock(KafkaServices::class.java)
        val rssFeedScheduler = RssFeedScheduler(rssFeedServiceMock, kafkaServicesMock)

        rssFeedScheduler.scheduleRSSFeed()

        Mockito.verify(rssFeedServiceMock).readRssFeed("latest")
    }
}
