package me.flashka.web.taxi.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import me.flashka.web.taxi.repository.model.BaseModel
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(p0: HttpServletRequest?, p1: HttpServletResponse?, p2: AuthenticationException?) {
        p1?.characterEncoding = "UTF-8"
        p1?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Авторизируйтесь для доступа в ресурсу")
    }

}