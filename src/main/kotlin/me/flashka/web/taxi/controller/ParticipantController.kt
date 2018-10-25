package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.ParticipantRepository
import me.flashka.web.taxi.repository.dto.FrontWinnerDTO
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.OfferModel
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*

@RestController
@RequestMapping(value = ["/participant"])
class ParticipantController(
        private val participantRepository: ParticipantRepository
) {

    @GetMapping("/participant_form")
    @Secured("ROLE_ADMIN")
    fun getUsers(@RequestParam("id") id: Long): ModelAndView {
        val modelAndView = ModelAndView("participant_form")
        val participants = participantRepository.findAllByOfferOrderByDateDesc(OfferModel(id))
        modelAndView.addObject("participants", participants)
        if (participants.isNotEmpty()) modelAndView.addObject("offerModel", participants[0].offer!!)
        return modelAndView
    }

    @GetMapping("/winner/get_list")
    fun getWinners(): BaseModel<List<FrontWinnerDTO>> {
        val winners: MutableList<FrontWinnerDTO> = ArrayList()
        val participants = participantRepository.findAllByWinner(true)
        participants.forEach {
            winners.add(FrontWinnerDTO(it))
        }
        return BaseModel(200, "", winners)
    }
}