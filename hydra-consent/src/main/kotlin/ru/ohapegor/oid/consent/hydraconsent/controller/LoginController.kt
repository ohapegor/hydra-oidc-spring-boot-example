package ru.ohapegor.oid.consent.hydraconsent.controller

import mu.KLogging
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.ohapegor.oid.consent.hydraconsent.client.HydraClient
import ru.ohapegor.oid.consent.hydraconsent.config.ACCESS_TOKEN_COOKIE
import ru.ohapegor.oid.consent.hydraconsent.controller.dto.LoginRequest
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
    fun login(@RequestParam(name = "login_challenge") loginChallenge: String, res: HttpServletResponse, principal: Principal?): String {
        val loginData = hydraClient.getLoginRequest(loginChallenge)
        when {
            loginData.skip -> {
                val acceptResp = hydraClient.acceptLoginRequest(loginData.subject, loginChallenge)
                logger.info { ">>auto acceptResp : $acceptResp" }
                return  "redirect:"+acceptResp.redirectTo
            }
            principal == null -> {
                logger.debug { ">> not authorized" }
                return "login"
            }
            else -> {
                val acceptResp = hydraClient.acceptLoginRequest(principal.name, loginChallenge)
                logger.info { ">>principal acceptResp : $acceptResp" }
                return  "redirect:"+acceptResp.redirectTo
            }
        }
    }

    @PostMapping("/login", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun login(loginRequest: LoginRequest, res: HttpServletResponse, principal: Principal) {
        logger.debug { ">> login request: $loginRequest" }

        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                loginRequest.email, loginRequest.password
        ))
        SecurityContextHolder.getContext().authentication = authentication
        val accessToken = jwtService.generateAccessToken(authentication.principal as User)
        res.addCookie(Cookie(ACCESS_TOKEN_COOKIE, accessToken))
        val acceptResp = hydraClient.acceptLoginRequest(principal.name, loginRequest.loginChallenge)
        logger.info { ">>principal acceptResp : $acceptResp" }
        res.sendRedirect(acceptResp.redirectTo)
    }


}