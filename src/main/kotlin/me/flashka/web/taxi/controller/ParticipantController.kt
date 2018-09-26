package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.ParticipantRepository
import me.flashka.web.taxi.repository.dto.FrontWinnerDTO
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.model.ParticipantModel
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/participant"])
class ParticipantController(
        val participantRepository: ParticipantRepository,
        val offerRepository: OfferRepository
) {

    @GetMapping("/participant_form")
    fun getUsers(@RequestParam("id") id: Long): ModelAndView {
        val modelAndView = ModelAndView("participant_form")
        val participants = participantRepository.findAllByOfferOrderByDateDesc(OfferModel(id))
        modelAndView.addObject("participants", participants)
        if (participants.isNotEmpty()) modelAndView.addObject("offerModel", participants[0].offer!!)
        return modelAndView
    }

    @PostMapping("/set")
    fun setUser(@Valid @RequestBody participantModel: ParticipantModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val offerModel = offerRepository.findById(participantModel.offer?.id!!).get()
        if (!offerModel.active)
            return BaseModel(400, "Акция закончена. Регистрация невозможна.")
        if (participantRepository.existsByUserAndOffer(participantModel.user!!, participantModel.offer))
            return BaseModel(400, "Участник уже зарегистрирован в акции")
        participantRepository.save(participantModel)
        offerModel.participants++
        offerRepository.save(offerModel)
        return BaseModel(200, "Участник зарегистрирован в акции")
    }

    @GetMapping("/winner/get_list")
    fun getWinners(): BaseModel<List<FrontWinnerDTO>> {
        val winners: MutableList<FrontWinnerDTO> = ArrayList()
        val participants = participantRepository.findAll()
        participants.forEach {
            winners.add(FrontWinnerDTO(it))
        }
        return BaseModel(200, "", winners)
    }

}