package com.example.crawler.serviceTests

import com.example.crawler.service.RssFeedScheduler
import com.example.crawler.service.RssFeedService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RssFeedSchedulerTests {

    @Test
    fun scheduleRSSFeedShouldSaveTheNewestArticles() {
        val rssFeedServiceMock = Mockito.mock(RssFeedService::class.java)
        val rssFeedScheduler = RssFeedScheduler(rssFeedServiceMock)

        rssFeedScheduler.scheduleRSSFeed()

        Mockito.verify(rssFeedServiceMock).readRssFeed("latest")
    }
}
