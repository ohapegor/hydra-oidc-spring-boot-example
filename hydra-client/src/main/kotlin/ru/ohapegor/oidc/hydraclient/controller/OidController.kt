package ru.ohapegor.oidc.hydraclient.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest


@RestController
class OidController(
        private val restTemplate: RestTemplate
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/oidc-principal")
    fun getOidcUserPrincipal(@AuthenticationPrincipal principal: OidcUser): OidcUser {
        return principal
    }

    @GetMapping("/callback")
    fun callback(@RequestParam code: String, req: HttpServletRequest): String? {
        // if (code.isNotBlank()) return code

        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("code", code)
        map.add("grant_type", "authorization_code")
        map.add("redirect_uri", "http://localhost:8080/callback")
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
        val entity = HttpEntity(map, headers)

        val response: ResponseEntity<String> = restTemplate.exchange("http://localhost:4444/oauth2/token",
                HttpMethod.POST,
                entity,
                String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            return response.toString()
        }
        //OAuth2AccessTokenResponse

        return response.body
    }


}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AccessTokenResponse(
        val accessToken: String,
        val expiresIn: String,
        val tokenType: String,
        val scope: String
)