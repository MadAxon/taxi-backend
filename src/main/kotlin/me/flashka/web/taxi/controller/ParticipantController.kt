package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.ParticipantRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.ParticipantModel
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/offer/user"])
class ParticipantController(
        val participantRepository: ParticipantRepository,
        val offerRepository: OfferRepository
) {

    @GetMapping("/get")
    fun get(): BaseModel<List<ParticipantModel>> {
        return BaseModel(200, "", participantRepository.findAll())
    }

    @PostMapping("/set")
    fun set(@Valid @RequestBody participantModel: ParticipantModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (!offerRepository.findById(participantModel.offer?.id!!).get().active)
            return BaseModel(400, "Акция закончена. Регистрация невозможна.")
        if (participantRepository.existsByUserAndOffer(participantModel.user!!, participantModel.offer))
            return BaseModel(400, "Участник уже зарегистрирован в акции")
        participantRepository.save(participantModel)
        return BaseModel(200, "Участник зарегистрирован в акции")
    }

}