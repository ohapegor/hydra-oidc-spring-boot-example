package ru.ohapegor.oid.consent.hydraconsent.service

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.ohapegor.oid.consent.hydraconsent.model.User
import java.security.SignatureException
import java.util.Date


@Component
class JwtService(
        @Value("\${jwt.secret}")
        private val jwtSecret: String,
        @Value("\${jwt.issuer}")
        private val jwtIssuer: String
) {

    companion object : KLogging()

    fun generateAccessToken(user: User): String {
        return Jwts.builder()
                .setSubject(user.id)
                .setIssuer(jwtIssuer)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserId(token: String): String = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body.subject

    fun getExpirationDate(token: String): Date = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body.expiration

    fun validate(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (ex: SignatureException) {
            logger.error { "Invalid JWT signature - ${ex.message}" }
        } catch (ex: MalformedJwtException) {
            logger.error { "Invalid JWT token - ${ex.message}" }
        } catch (ex: ExpiredJwtException) {
            logger.error { "Expired JWT token - ${ex.message}" }
        } catch (ex: UnsupportedJwtException) {
            logger.error { "Unsupported JWT token - ${ex.message}" }
        } catch (ex: IllegalArgumentException) {
            logger.error { "JWT claims string is empty - ${ex.message}" }
        }
        return false
    }
}