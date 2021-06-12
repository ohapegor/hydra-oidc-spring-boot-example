package ru.ohapegor.oid.consent.hydraconsent.controller

import mu.KLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class PageController {

    companion object : KLogging()

    @GetMapping("/secured")
    fun securedPage() = "securedPage"

    @GetMapping("/free")
    fun freePage() = "freePage"

}