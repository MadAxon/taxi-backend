package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.SmsRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.model.SmsModel
import me.flashka.web.taxi.repository.model.UserModel
import me.flashka.web.taxi.repository.request.LoginRequest
import me.flashka.web.taxi.repository.request.SmsRequest
import me.flashka.web.taxi.repository.response.SmsResponse
import me.flashka.web.taxi.service.SmsCleanerService
import me.flashka.web.taxi.service.UserDetailServiceImpl
import me.flashka.web.taxi.service.UserService
import org.apache.commons.lang3.RandomStringUtils
import org.apache.logging.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/auth"])
class AuthController(
        private val userRepository: UserRepository,
        private val userService: UserService,
        private val authenticationManager: AuthenticationManager,
        private val tokenProvider: JwtTokenProvider,
        private val userDetailServiceImpl: UserDetailServiceImpl,
        private val smsRepository: SmsRepository,
        private val smsCleanerService: SmsCleanerService
) {

    val logger: Logger = LoggerFactory.getLogger(AuthController::class.java)

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
            return BaseModel(200, "", jwt)
        }
        return BaseModel(401, "Неверный номер или пароль")
    }

    @GetMapping("/login_form")
    fun showLoginForm(): ModelAndView {
        return ModelAndView("login_form", "loginRequest", LoginRequest())
    }

    @PostMapping("/login_form/send")
    fun loginAsAdmin(@Valid @ModelAttribute("loginRequest")loginRequest: LoginRequest, bindingResult: BindingResult
                     , modelMap: ModelMap): ModelAndView {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null) {
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
            return ModelAndView("login_form", "loginRequest", loginRequest)
        }
        val baseModel = login(loginRequest, bindingResult)
        return if (baseModel.status == 200) {
            return ModelAndView("redirect:/offer/main_form", "offers", ArrayList<OfferModel>())
        }
        else {
            modelMap.addAttribute("message", baseModel.message)
            return ModelAndView("login_form", "loginRequest", loginRequest)
        }
    }

    @PostMapping("/registration")
    fun registration(@Valid @RequestBody userModel: UserModel, bindingResult: BindingResult
                     , request: HttpServletRequest): BaseModel<String> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (userRepository.existsByPhoneNumber(userModel._phoneNumber))
            return BaseModel(400, "Номер телефона уже используется")
        if (userRepository.existsByCarNumber(userModel._carNumber))
            return BaseModel(400, "Номер машины уже зарегистрирован в системе")
        if (userModel.city?.id!! <= 0)
            return BaseModel(400, "Укажите город")
        /*if (smsRepository.existsByPhoneNumberAndUntilExpiredDateBefore(userModel.phoneNumber, Date())
                || smsRepository.existsByPhoneNumberAndIp(userModel.phoneNumber, request.remoteAddr)) {
            return BaseModel(400, "Sms сообщение уже отправлено на ваш номер телефона")
        }

        if (userModel.code.isNullOrBlank()) {
            val code = generateCode()
            if (sendSms(userModel.id, userModel.phoneNumber!!, code) == "accepted") {
                smsRepository.save(SmsModel(userModel.phoneNumber, code, request.remoteAddr))
                smsCleanerService.deleteEntityFromTableWithDelay(userModel, code)
                return BaseModel(201, "На ваш номер телефона выслано sms сообщение с кодом")
            }
            return BaseModel(400, "Неполадки в sms сервисе. Повторите попытку позже")
        }*/

        if (!smsRepository.existsByPhoneNumberAndCode(userModel.phoneNumber, userModel.code!!))
            return BaseModel(400, "Указан неверный код!")
        return BaseModel(200, "Регистрация прошла успешно", userService.save(userModel))
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException::class)
    fun invalidLoginPassword(): BaseModel<Any> {
        return BaseModel(401, "Неверный номер или пароль")
    }

    fun sendSms(userId: Long, phone: String, code: String): String {
        val restTemplate = RestTemplate()
        val request = HttpEntity(SmsRequest(userId.toString(), phone, code))
        val smsResponse = restTemplate.postForObject(
                "https://api.smsbliss.net/messages/v2/send.json",
                request,
                SmsResponse::class.java)
        if (smsResponse?.messages?.size!! > 0) {
            return smsResponse.messages[0].status
        }
        return ""
    }

    fun generateCode(): String {
        val possibleCharacters = StringBuilder("0123456789")
                .toString().toCharArray()
        return RandomStringUtils.random(4, 0, possibleCharacters.size - 1
                , false
                , false
                , possibleCharacters, null)
    }

}