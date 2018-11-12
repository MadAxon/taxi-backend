package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.SmsRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.SmsModel
import me.flashka.web.taxi.repository.model.UserModel
import me.flashka.web.taxi.repository.request.*
import me.flashka.web.taxi.repository.response.SmsResponse
import me.flashka.web.taxi.service.SmsCleanerService
import me.flashka.web.taxi.service.UserService
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.http.HttpEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/user"])
class UserController(
        private val userRepository: UserRepository,
        private val tokenProvider: JwtTokenProvider,
        private val smsRepository: SmsRepository,
        private val smsCleanerService: SmsCleanerService
        ) {

    @GetMapping("/get_list")
    @Secured("ROLE_ADMIN")
    fun getUsers(): ModelAndView {
        return ModelAndView("user_form", "users", userRepository.findAll())
    }

    @PostMapping("/update")
    fun updateUser(@RequestHeader("Authorization") token: String
                   , @Valid @RequestBody userRequest: UserUpdateRequest
                   , bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        return if (userModel == null) BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        else {
            userModel.firstName = userRequest.firstName
            userModel.lastName = userRequest.lastName
            userModel.patronymic = userRequest.patronymic
            userModel.city = userRequest.city
            userRepository.save(userModel)
            BaseModel(200, "Профиль успешно изменен")
        }
    }

    @GetMapping("/get")
    fun getUser(@RequestHeader("Authorization") token: String): BaseModel<UserModel> {
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        return if (userModel != null) BaseModel(200, "", userModel)
        else BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
    }


    @PostMapping("/password/change")
    fun changePassword(@RequestHeader("Authorization") token: String
                       , @Valid @RequestBody request: PasswordChangingRequest?
                       , bindingResult: BindingResult)
            : BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (!request?.newPassword.equals(request?.newPasswordAgain))
            return BaseModel(400, "Новые пароли не совпадают")
        val bCryptPasswordEncoder = BCryptPasswordEncoder()
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        if (bCryptPasswordEncoder.matches(request?.oldPassword, userModel?.password)) {
            if (userModel != null) {
                userModel.password = bCryptPasswordEncoder.encode(request?.newPassword)
                userRepository.save(userModel)
                return BaseModel(200, "Пароль успешно изменен")
            }
            return BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        }
        return BaseModel(400, "Текущий пароль неверен")
    }

    @PostMapping("/phone/change")
    fun changePhone(@RequestHeader("Authorization") token: String
                    , @Valid @RequestBody request: PhoneChangingRequest?
                    , bindingResult: BindingResult) : BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        if (userModel == null)
            return BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        else {
            if (userRepository.existsByPhoneNumber(request?._newPhone))
                return BaseModel(400, "Номер телефона уже используется")
            if (smsRepository.existsByPhoneNumberAndUntilExpiredDateBefore(userModel.phoneNumber, Date())) {
                return BaseModel(400, "Sms сообщение уже отправлено на ваш номер телефона")
            }

            if (request?.code.isNullOrBlank()) {
                val code = generateCode()
                if (sendSms(userModel.id, userModel.phoneNumber!!, code) == "accepted") {
                    smsRepository.save(SmsModel(userModel.phoneNumber, code))
                    smsCleanerService.deleteEntityFromTableWithDelay(userModel, code)
                    return BaseModel(201, "На ваш номер телефона выслано sms сообщение с кодом")
                }
                return BaseModel(400, "Неполадки в sms сервисе. Повторите попытку позже")
            }

            if (!smsRepository.existsByPhoneNumberAndCode(userModel.phoneNumber, request?.code!!))
                return BaseModel(400, "Указан неверный код!")
            userModel.phoneNumber = request._newPhone
            userRepository.save(userModel)
            return BaseModel(200, "Номер телефона успешно изменен")
        }
    }

    @PostMapping("/car_number/change")
    fun changeCarNumber(@RequestHeader("Authorization") token: String
                        , @Valid @RequestBody request: CarNumberChangingRequest?
                        , bindingResult: BindingResult) : BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        if (userModel == null)
            return BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        else {
            if (userRepository.existsByCarNumber(request?._carNumber))
                return BaseModel(400, "Номер машины уже зарегистрирован в системе")
            val bCryptPasswordEncoder = BCryptPasswordEncoder()
            if (bCryptPasswordEncoder.matches(request?.password, userModel.password)) {
                userModel.carNumber = request?._carNumber
                userRepository.save(userModel)
                return BaseModel(200, "Номер машины успешно изменен")
            }
            return BaseModel(400, "Текущий пароль неверен")
        }
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