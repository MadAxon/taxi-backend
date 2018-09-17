package me.flashka.web.taxi.jwt

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.net.Authenticator
import java.util.*

@Component
class JwtTokenProvider(

        @Value("JWTSuperSecretKey")
        val jwtSecret: String,

        @Value("604800000")
        val jwtExpirationInMs: Long
) {

    fun generateToken(userDetails: UserDetails): String {
        val date = Date()
        val expiryDate = Date(date.time + jwtExpirationInMs)

        return Jwts.builder()
                .setSubject(userDetails.username)
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun phoneNumberFromJWT(token: String): String {
        val claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body
        return claims.subject
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (signatureException: SignatureException) {
            //Invalid JWT signature
        } catch (malformedJwtException: MalformedJwtException) {
            //Invalid JWT token
        } /*catch (ExpiredJwtException: ExpiredJwtException) {
            //Expired JWT token
        } */catch (unsupportedJwtException: UnsupportedJwtException) {
            //Unsupported JWT token
        } catch (illegalArgumentException: IllegalArgumentException) {
            //JWT claims string is empty
        }
        return false
    }

}