package me.flashka.web.taxi

import me.flashka.web.taxi.jwt.JwtAuthenticationFilter
import me.flashka.web.taxi.jwt.JwtAuthenticationEntryPoint
import me.flashka.web.taxi.jwt.JwtTokenProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
class WebSecurityConfig(
        @Qualifier("userDetailServiceImpl")
        private val userDetailsService: UserDetailsService,
        private val jwtTokenProvider: JwtTokenProvider
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun configure(http: HttpSecurity?) {
        http
                ?.csrf()?.disable()
                ?.exceptionHandling()
                ?.authenticationEntryPoint(authenticationEntryPoint())
                /*?.and()
                ?.sessionManagement()
                ?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/
                ?.and()
                ?.authorizeRequests()
                ?.antMatchers("/resources/**", "/auth/**")
                ?.permitAll()?.anyRequest()?.authenticated()
                ?.and()
                ?.logout()
                ?.permitAll()
        http?.addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider, userDetailsService)
                , UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(bCryptPasswordEncoder())
    }


    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun authenticationEntryPoint(): JwtAuthenticationEntryPoint = JwtAuthenticationEntryPoint()

}