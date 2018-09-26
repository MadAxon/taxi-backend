package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.UserModel
import me.flashka.web.taxi.repository.request.PasswordChangingRequest
import me.flashka.web.taxi.service.UserService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/user"])
class UserController(
        private val userRepository: UserRepository,
        private val userService: UserService,
        private val tokenProvider: JwtTokenProvider
        ) {

    @GetMapping("/get_list")
    //@Secured("ROLE_ADMIN")
    fun getUsers(): ModelAndView {
        return ModelAndView("user_form", "users", userRepository.findAll())
    }

    @PostMapping("/update")
    fun updateUser(@Valid @RequestBody userModel: UserModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        return if (userService.save(userModel))
            BaseModel(200, "Профиль успешно изменен")
        else BaseModel(400, "Номер телефона уже используется")
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

}