package me.flashka.web.taxi.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(p0: HttpServletRequest?, p1: HttpServletResponse?, p2: AuthenticationException?) {
        p1?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You aren't authorized. Access denied.")
    }

}