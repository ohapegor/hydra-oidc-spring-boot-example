package ru.ohapegor.oid.consent.hydraconsent.controller

import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.ohapegor.oid.consent.hydraconsent.client.HydraClient
import ru.ohapegor.oid.consent.hydraconsent.config.ACCESS_TOKEN_COOKIE
import ru.ohapegor.oid.consent.hydraconsent.controller.dto.LoginRequest
import ru.ohapegor.oid.consent.hydraconsent.model.User
import ru.ohapegor.oid.consent.hydraconsent.service.JwtService
import java.security.Principal
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@Controller
class LoginController(
        private val hydraClient: HydraClient,
        private val authenticationManager: AuthenticationManager,
        private val jwtService: JwtService,
) {

    companion object : KLogging()

    @GetMapping("/login")
    fun login(@RequestParam(name = "login_challenge") loginChallenge: String, model: Model, principal: Principal?): String {
        val loginData = hydraClient.getLoginRequest(loginChallenge)
        when {
            loginData.skip -> {
                val acceptResp = hydraClient.acceptLoginRequest(loginData.subject, loginChallenge)
                logger.info { ">>skip acceptResp : $acceptResp" }
                return "redirect:" + acceptResp.redirectTo
            }
            principal == null -> {
                logger.debug { ">> not authorized" }
                model.addAttribute("loginChallenge", loginChallenge)
                return "login"
            }
            else -> {
                val acceptResp = hydraClient.acceptLoginRequest(principal.name, loginChallenge)
                logger.info { ">>logged in principal acceptResp : $acceptResp" }
                return "redirect:" + acceptResp.redirectTo
            }
        }
    }

    @PostMapping("/login", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(loginRequest: LoginRequest, res: HttpServletResponse) {
        logger.debug { ">> login request: $loginRequest" }
        if (loginRequest.submit == "Deny access") {
            val rejectResp = hydraClient.rejectLoginRequest(loginRequest.loginChallenge)
            res.sendRedirect(rejectResp.redirectTo)
            return
        }
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                loginRequest.email, loginRequest.password
        ))
        SecurityContextHolder.getContext().authentication = authentication
        val user = authentication.principal as User
        val accessToken = jwtService.generateAccessToken(user)
        res.addCookie(Cookie(ACCESS_TOKEN_COOKIE, accessToken))
        val acceptResp = hydraClient.acceptLoginRequest(user.id, loginRequest.loginChallenge)
        logger.info { ">>acceptResp : $acceptResp" }
        res.sendRedirect(acceptResp.redirectTo)
    }


}