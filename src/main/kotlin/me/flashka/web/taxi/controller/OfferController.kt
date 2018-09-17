package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.OfferModel
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/offer"])
class OfferController(
        val offerRepository: OfferRepository
) {

    @PostMapping("/set")
    fun set(@Valid @RequestBody offerModel: OfferModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (offerModel.startDate.after(offerModel.endDate))
            return BaseModel(400, "Дата начала акции установлена после её окончания")
        if (offerModel.startDate.compareTo(offerModel.endDate) == 0)
            return BaseModel(400, "Дата и время начала и конца акции совпадают")
        offerRepository.save(offerModel)
        return BaseModel(200, "Акция создана")
    }

    @PostMapping("/get")
    fun get(): List<OfferModel> = offerRepository.findAll()

}