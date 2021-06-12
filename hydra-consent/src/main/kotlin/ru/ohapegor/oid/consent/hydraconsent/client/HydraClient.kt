package ru.ohapegor.oid.consent.hydraconsent.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.ohapegor.oid.consent.hydraconsent.client.dto.AcceptLoginRequestBody
import ru.ohapegor.oid.consent.hydraconsent.client.dto.AcceptLoginResponsetBody
import ru.ohapegor.oid.consent.hydraconsent.client.dto.LoginData

@Component
class HydraClient(
        private val restTemplate: RestTemplate
) {
    fun getLoginRequest(loginChallenge: String): LoginData {
        return restTemplate.getForObject("/oauth2/auth/requests/login?challenge=$loginChallenge", LoginData::class.java)
                ?: error("missing LoginData")
    }

    fun confirmLogin(subject: String, loginChallenge: String): AcceptLoginResponsetBody {
        val resp = restTemplate.exchange(
                "/oauth2/auth/requests/login?challenge=$loginChallenge",
                HttpMethod.PUT,
                HttpEntity(AcceptLoginRequestBody(subject = subject)),
                AcceptLoginResponsetBody::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("confirmLogin response failed with status ${resp.statusCode}")
    }

    fun acceptLoginRequest(subject: String, loginChallenge: String, remember: Boolean = false): AcceptLoginResponsetBody {
        val resp = restTemplate.exchange(
                "/oauth2/auth/requests/login/accept?challenge=$loginChallenge",
                HttpMethod.PUT,
                HttpEntity(AcceptLoginRequestBody(subject = subject, remember = remember)),
                AcceptLoginResponsetBody::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("confirmLogin response failed with status ${resp.statusCode}")
    }
}