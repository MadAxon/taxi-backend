package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.UserModel
import me.flashka.web.taxi.repository.request.LoginRequest
import me.flashka.web.taxi.repository.request.PasswordChangingRequest
import me.flashka.web.taxi.service.UserDetailServiceImpl
import me.flashka.web.taxi.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/auth"])
class AuthController(
        private val userService: UserService,
        private val authenticationManager: AuthenticationManager,
        private val tokenProvider: JwtTokenProvider,
        private val userDetailServiceImpl: UserDetailServiceImpl
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest, bindingResult: BindingResult): BaseModel<String> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)

        val userDetails = userDetailServiceImpl.loadUserByUsername(loginRequest._phoneNumber)
        if (userDetails != null) {
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(userDetails
                    , loginRequest.password, userDetails.authorities))
            SecurityContextHolder.getContext().authentication = authentication
            val jwt = tokenProvider.generateToken(userDetails)
            return BaseModel(0, "", jwt)
        }
        return BaseModel(400, "Номер телефона не зарегистрирован")
    }

    @PostMapping("/registration")
    fun registration(@Valid @RequestBody userModel: UserModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        return if (userService.save(userModel))
            BaseModel(200, "Регистрация прошла успешно")
        else BaseModel(400, "Номер телефона уже используется")
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException::class)
    fun invalidLoginPassword(): BaseModel<Any> {
        return BaseModel(401, "Неверный номер или пароль")
    }

}