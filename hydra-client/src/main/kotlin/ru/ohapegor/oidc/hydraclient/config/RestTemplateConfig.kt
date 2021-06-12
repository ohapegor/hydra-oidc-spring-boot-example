package ru.ohapegor.oidc.hydraclient.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate() = RestTemplate().apply {
      //  interceptors.add(BasicAuthenticationInterceptor("ali-client-id", "ali-client-secret"))
    }
}