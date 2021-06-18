package com.example.crawler.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@ConditionalOnProperty(
    value = ["app.scheduling.enable"], havingValue = "true", matchIfMissing = true
)
@Configuration
@Component
@EnableScheduling
class RssFeedScheduler(@Autowired val rssFeedService: RssFeedService) {

    @Scheduled(fixedDelay = 5000, initialDelay = 30000)
    fun scheduleRSSFeed(){
        val arrayOfSections = listOf(
            "latest", "topnews", "section/mediathek", "section/videos", "section/politik",
            "section/wirtschaft", "section/wirtschaft/bilanz", "section/finanzen",
            "section/wirtschaft/webwelt", "section/wissenschaft", "section/kultur", "section/sport",
            "section/icon", "section/gesundheit", "section/vermischtes", "section/motor",
            "section/reise", "section/regionales", "section/debatte"
        )
        arrayOfSections.forEach{
            rssFeedService.saveRssFeedToDB(rssFeedService.readRssFeed(it))
        }
    }
}
