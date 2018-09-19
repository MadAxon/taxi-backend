package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.service.TimerService
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/offer"])
class OfferController(
        val offerRepository: OfferRepository,
        val timerService: TimerService
) {

    @PostMapping("/set")
    fun set(@Valid @RequestBody offerModel: OfferModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (offerModel.startDate?.after(offerModel.endDate)!!)
            return BaseModel(400, "Дата начала акции установлена после её окончания")
        if (offerModel.startDate.compareTo(offerModel.endDate) == 0)
            return BaseModel(400, "Дата и время начала и конца акции совпадают")
        offerRepository.save(offerModel)
        timerService.runTimer()
        return BaseModel(200, "Акция создана")
    }

    @GetMapping("/get")
    fun get(): BaseModel<List<OfferModel>> {
        return BaseModel(200, "", offerRepository.findAll())
    }

}