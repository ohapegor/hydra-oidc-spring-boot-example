package ru.ohapegor.oid.consent.hydraconsent.controller

import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.ohapegor.oid.consent.hydraconsent.client.HydraClient
import ru.ohapegor.oid.consent.hydraconsent.controller.dto.ConsentRequest
import ru.ohapegor.oid.consent.hydraconsent.service.JwtService
import ru.ohapegor.oid.consent.hydraconsent.service.SecurityUtils.currentUser
import javax.servlet.http.HttpServletResponse


@Controller
class ConsentController(
        private val hydraClient: HydraClient,
        private val authenticationManager: AuthenticationManager,
        private val jwtService: JwtService,
) {

    companion object : KLogging()

    @GetMapping("/consent")
    fun consent(@RequestParam(name = "consent_challenge") consentChallenge: String, model: Model): String {
        val hydraConsentRequest = hydraClient.getConsentRequest(consentChallenge)
        val user = currentUser()
        when {
            hydraConsentRequest.skip -> {
                val acceptResp = hydraClient.acceptConsentRequest(
                        consentChallenge = consentChallenge,
                        scopes = hydraConsentRequest.requestedScope,
                        grantAccessTokenAudience = hydraConsentRequest.requestedAccessTokenAudience)
                logger.info { ">>skip consent, consentRequest : $acceptResp" }
                return "redirect:" + acceptResp.redirectTo
            }
            else -> {
                model.addAttribute("userName", "${user.firstname} ${user.lastname}")
                model.addAttribute("consentChallenge", consentChallenge)
                model.addAttribute("appName", hydraConsentRequest.client.clientName)
                model.addAttribute("scopes", hydraConsentRequest.requestedScope)
                return "consent"
            }
        }
    }

    @PostMapping("/consent", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(consentRequest: ConsentRequest, res: HttpServletResponse) {
        val hydraConsentRequest = hydraClient.getConsentRequest(consentRequest.consentChallenge)
        logger.debug { ">> login request: $consentRequest" }
        val acceptResp = hydraClient.acceptConsentRequest(
                consentChallenge = consentRequest.consentChallenge,
                scopes = consentRequest.grantScope,
                grantAccessTokenAudience = hydraConsentRequest.requestedAccessTokenAudience,
                remember = consentRequest.remember)
        logger.info { ">>acceptConsentResp : $acceptResp" }
        res.sendRedirect(acceptResp.redirectTo)
    }


}