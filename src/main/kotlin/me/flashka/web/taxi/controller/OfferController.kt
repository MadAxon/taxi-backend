package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.request.OfferRequest
import me.flashka.web.taxi.service.TimerService
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/offer"])
class OfferController(
        val offerRepository: OfferRepository,
        val timerService: TimerService
) {

    @PostMapping("/set")
    //@Secured("ROLE_ADMIN")
    fun set(@Valid @RequestBody offerModel: OfferModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        if (offerModel.startDate?.after(offerModel.endDate)!!)
            return BaseModel(400, "Дата начала акции установлена после её окончания")
        if (offerModel.startDate.compareTo(offerModel.endDate) == 0)
            return BaseModel(400, "Дата и время начала и конца акции совпадают")
        if (offerModel.endDate?.before(Date())!!)
            return BaseModel(400, "Время окончания акции находится раньше настоящего времени")
        offerRepository.save(offerModel)
        timerService.runTimer()
        return BaseModel(200, "Акция создана")
    }

    @PostMapping("/get_list")
    fun get(@RequestBody offerRequest: OfferRequest?): BaseModel<List<OfferModel>> {
        return if (offerRequest?.cityId == null || offerRequest.cityId == 0L)
            BaseModel(200, "", offerRepository.findAll())
        else BaseModel(200, "", offerRepository.findAllByCity(CityModel(offerRequest.cityId)))
    }

}