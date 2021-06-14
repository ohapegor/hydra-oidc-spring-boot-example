package ru.ohapegor.oid.consent.hydraconsent.client

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptConsentRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAcceptLoginRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraAddress
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraCommonRedirectResponse
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraConsentRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraIdToken
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraLoginRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraRejectRequest
import ru.ohapegor.oid.consent.hydraconsent.client.dto.HydraSession
import ru.ohapegor.oid.consent.hydraconsent.service.SecurityUtils.currentUser

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

    fun acceptLoginRequest(subject: String, loginChallenge: String, remember: Boolean = true): HydraCommonRedirectResponse {
        val resp = restTemplate.exchange(
                "$LOGIN_PATH/accept?login_challenge=$loginChallenge",
                HttpMethod.PUT,
                HttpEntity(HydraAcceptLoginRequest(subject = subject, remember = remember)),
                HydraCommonRedirectResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("acceptLogin response failed with status ${resp.statusCode}")
    }

    fun rejectLoginRequest(loginChallenge: String): HydraCommonRedirectResponse {
        val resp = restTemplate.exchange(
                "$LOGIN_PATH/reject?login_challenge=$loginChallenge",
                HttpMethod.PUT,
                HttpEntity(
                        HydraRejectRequest(
                                error = "access_denied",
                                errorDescription = "The resource owner denied the request"
                        )),
                HydraCommonRedirectResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("rejectLogin response failed with status ${resp.statusCode}")
    }

    fun rejectConsentRequest(consentChallenge: String): HydraCommonRedirectResponse {
        val resp = restTemplate.exchange(
                "$CONSENT_PATH/reject?consent_challenge=$consentChallenge",
                HttpMethod.PUT,
                HttpEntity(
                        HydraRejectRequest(
                                error = "access_denied",
                                errorDescription = "The resource owner denied the request"
                        )),
                HydraCommonRedirectResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("rejectConsent response failed with status ${resp.statusCode}")
    }

    fun getConsentRequest(consentChallenge: String): HydraConsentRequest {
        return restTemplate.getForObject("$CONSENT_PATH?consent_challenge=$consentChallenge", HydraConsentRequest::class.java)
                ?: error("missing LoginData")
    }

    fun acceptConsentRequest(
            consentChallenge: String,
            scopes: List<String>,
            grantAccessTokenAudience: List<String>,
            remember: Boolean = true): HydraCommonRedirectResponse {
        val user = currentUser()
        val resp = restTemplate.exchange(
                "$CONSENT_PATH/accept?consent_challenge=$consentChallenge",
                HttpMethod.PUT,
                HttpEntity(HydraAcceptConsentRequest(
                        grantScope = scopes,
                        grantAccessTokenAudience = grantAccessTokenAudience,
                        remember = remember,
                        session = HydraSession(idToken = HydraIdToken(
                                subject = user.id,
                                email = user.email,
                                familyName = user.lastname,
                                nickName = user.email,
                                givenName = user.firstname,
                                fullName = "${user.firstname} ${user.lastname}",
                                address = HydraAddress(country = "RU", region = "Moscow", streetAddress = "Kremlin"),
                                locale = "ru_RU",
                                phoneNumber = "+79876543210",
                                //and so on
                        )))),
                HydraCommonRedirectResponse::class.java,
        )
        if (resp.statusCode.is2xxSuccessful) return resp.body!!
        error("acceptConsent failed with status ${resp.statusCode}")
    }

}