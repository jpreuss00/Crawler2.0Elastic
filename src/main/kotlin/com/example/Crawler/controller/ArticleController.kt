package com.example.crawler.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.io.Console

@Controller
class ArticleController {
   @GetMapping("/test")
    fun controller() : String {
       print("works...")
       return ""
    }
}