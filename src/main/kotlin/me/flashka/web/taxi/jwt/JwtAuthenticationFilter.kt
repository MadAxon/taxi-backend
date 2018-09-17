package me.flashka.web.taxi.jwt

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(

        private val jwtTokenProvider: JwtTokenProvider,
        private val userDetailsService: UserDetailsService

) : OncePerRequestFilter() {

    override fun doFilterInternal(p0: HttpServletRequest, p1: HttpServletResponse, p2: FilterChain) {

        val jwt = getJwtFromRequest(p0)
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            val phoneNumber = jwtTokenProvider.phoneNumberFromJWT(jwt)
            val userDetails = userDetailsService.loadUserByUsername(phoneNumber)
            val authentication = UsernamePasswordAuthenticationToken(userDetails, userDetails.password, userDetails.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(p0)
            SecurityContextHolder.getContext().authentication = authentication
        }
        p2.doFilter(p0, p1)
    }

    fun getJwtFromRequest(p0: HttpServletRequest): String {
        val bearerToken = p0.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7)
        return ""
    }

}