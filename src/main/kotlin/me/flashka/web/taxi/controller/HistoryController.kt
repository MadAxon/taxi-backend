package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.HistoryRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.dto.FrontHistoryDTO
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.HistoryModel
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping(value = ["/history"])
class HistoryController(
        private val tokenProvider: JwtTokenProvider,
        private val userRepository: UserRepository,
        private val historyRepository: HistoryRepository
) {

    @GetMapping("/get_list")
    fun getUserHistory(@RequestHeader("Authorization") token: String): BaseModel<List<FrontHistoryDTO>> {
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
        val histories = ArrayList<FrontHistoryDTO>()
        historyRepository.findAllByUser(userModel!!).forEach {
            histories.add(FrontHistoryDTO(it))
        }
        return BaseModel(200, "", histories)
    }

    @GetMapping("/history_form")
    @Secured("ROLE_ADMIN")
    fun getHistory(): ModelAndView {
        return ModelAndView("history_form", "histories", historyRepository.findAll())
    }

}