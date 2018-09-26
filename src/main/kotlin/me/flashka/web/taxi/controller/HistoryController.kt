package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.HistoryOnly
import me.flashka.web.taxi.repository.HistoryRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.dto.FrontOfferDTO
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.HistoryModel
import me.flashka.web.taxi.repository.model.OfferModel
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/history"])
class HistoryController(
        private val tokenProvider: JwtTokenProvider,
        private val userRepository: UserRepository,
        private val historyRepository: HistoryRepository
) {

    @GetMapping("/get_list")
    fun getUserHistory(@RequestHeader("Authorization") token: String): BaseModel<List<HistoryOnly>> {
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        return if (userModel != null) BaseModel(200, "", historyRepository.findAllByUser(userModel))
        else BaseModel(400, "Не удалось заполучить историю пользователя. Возможно, он был удален")
    }

    @GetMapping("/get_list_admin")
    //@Secured("ROLE_ADMIN")
    fun getHistory(): ModelAndView {
        return ModelAndView("history_form", "histories", historyRepository.findAll())
    }

    @PostMapping("/set")
    fun setUserHistory(@Valid @RequestBody historyModel: HistoryModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        return BaseModel(200, "Операция добавлена в историю")
    }

}