package ru.ohapegor.oid.consent.hydraconsent.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class ClientConfig {

    @Bean
    @Qualifier("hydra")
    fun hydraRestTemplate(@Value("\${hydra.admin.url:http://localhost:4445}") hydraAdminUrl: String): RestTemplate =
            RestTemplateBuilder()
                    .rootUri(hydraAdminUrl)
                    .additionalInterceptors(LoggingRequestInterceptor())
                    .requestFactory { BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()) }
                    .build()

}