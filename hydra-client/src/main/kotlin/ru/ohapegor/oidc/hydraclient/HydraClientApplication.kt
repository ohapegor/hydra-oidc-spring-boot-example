package ru.ohapegor.oidc.hydraclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HydraClientApplication

fun main(args: Array<String>) {
    runApplication<HydraClientApplication>(*args)
}
