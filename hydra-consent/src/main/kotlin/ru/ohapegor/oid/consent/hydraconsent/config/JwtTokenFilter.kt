package ru.ohapegor.oid.consent.hydraconsent.config

import mu.KotlinLogging
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.ohapegor.oid.consent.hydraconsent.repository.UsersRepository
import ru.ohapegor.oid.consent.hydraconsent.service.JwtService
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


const val ACCESS_TOKEN_COOKIE = "jwtAccessToken"

@Component
class JwtTokenFilter(
        private val jwtService: JwtService,
        private val usersRepository: UsersRepository
) : OncePerRequestFilter() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override fun doFilterInternal(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  chain: FilterChain) {
        val accessToken = getAccessToken(request)
        if (accessToken == null) {
            chain.doFilter(request, response)
            return
        }

        // Get jwt token and validate
        if (!jwtService.validate(accessToken)) {
            chain.doFilter(request, response)
            return
        }

        // Get user identity and set it on the spring security context
        val userId = jwtService.getUserId(accessToken)
        val user = usersRepository.findById(userId).orElse(null)
        if (user == null) {
            log.error { "user not found by id $userId" }
        }
        val authentication = UsernamePasswordAuthenticationToken(
                user, null, user.authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
        chain.doFilter(request, response)
    }

    private fun getAccessToken(request: HttpServletRequest): String? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header.isNullOrBlank() || !header.startsWith("Bearer ")) {
            return request.cookies.firstOrNull { it.name == ACCESS_TOKEN_COOKIE }?.value
        }
        return header.split(" ").toTypedArray()[1].trim { it <= ' ' }
    }
}