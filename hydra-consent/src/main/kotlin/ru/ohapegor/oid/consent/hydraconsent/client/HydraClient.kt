package ru.ohapegor.oid.consent.hydraconsent.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptConsentRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptConsentResponse
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptLoginRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptLoginResponse
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraConsentRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraLoginRequest

@Component
class HydraClient(
        private val restTemplate: RestTemplate
) {

    companion object {
        private const val LOGIN_PATH = "/oauth2/auth/requests/login"
        private const val CONSENT_PATH = "/oauth2/auth/requests/consent"
    }

    fun getLoginRequest(loginChallenge: String): HydraLoginRequest {
        return restTemplate.getForObject("$LOGIN_PATH?login_challenge=$loginChallenge", HydraLoginRequest::class.java)
                ?: error("missing LoginData")
    }

    fun acceptLoginRequest(subject: String, loginChallenge: String, remember: Boolean = false): HydraAcceptLoginResponse {
        val resp = restTemplate.exchange(
                "$LOGIN_PATH/accept?login_challenge=$loginChallenge",
                HttpMethod.PUT,
                HttpEntity(HydraAcceptLoginRequest(subject = subject, remember = remember)),
                HydraAcceptLoginResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("acceptLogin response failed with status ${resp.statusCode}")
    }

    fun getConsentRequest(consentChallenge: String): HydraConsentRequest {
        return restTemplate.getForObject("$CONSENT_PATH?consent_challenge=$consentChallenge", HydraConsentRequest::class.java)
                ?: error("missing LoginData")
    }

    fun acceptConsentRequest(
            consentChallenge: String,
            scopes: List<String>,
            grantAccessTokenAudience: List<String>,
            remember: Boolean = false): HydraAcceptConsentResponse {
        val resp = restTemplate.exchange(
                "$CONSENT_PATH/accept?consent_challenge=$consentChallenge",
                HttpMethod.PUT,
                HttpEntity(HydraAcceptConsentRequest(
                        grantScope = scopes,
                        grantAccessTokenAudience = grantAccessTokenAudience,
                        remember = remember)),
                HydraAcceptConsentResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("acceptConsent failed with status ${resp.statusCode}")
    }
}