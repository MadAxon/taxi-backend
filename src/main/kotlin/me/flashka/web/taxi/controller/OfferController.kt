package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.CityRepository
import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.dto.FrontOfferDTO
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.request.OfferRequest
import me.flashka.web.taxi.service.TimerService
import org.springframework.lang.Nullable
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.validation.Valid
import kotlin.collections.ArrayList

@RestController
@RequestMapping(value = ["/offer"])
class OfferController(
        val offerRepository: OfferRepository,
        val timerService: TimerService,
        val cityRepository: CityRepository
) {

    @PostMapping("/set")
    @Secured("ROLE_ADMIN")
    fun setOffer(@Valid @ModelAttribute("offerModel") offerModel: OfferModel, bindingResult: BindingResult
            , modelMap: ModelMap): ModelAndView {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
        else if (offerModel.startDate?.after(offerModel.endDate)!!) {
            bindingResult.addError(FieldError("offerModel", "startDate"
                    , "Дата начала акции установлена после её окончания"))
            modelMap.addAttribute("error",  bindingResult.fieldErrors[0].defaultMessage!!)
        } else if (offerModel.startDate.compareTo(offerModel.endDate) == 0) {
            bindingResult.addError(FieldError("offerModel", "startDate"
                    , "Дата и время начала и конца акции совпадают"))
            bindingResult.addError(FieldError("offerModel", "endDate"
                    , "Дата и время начала и конца акции совпадают"))
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
        } else if (offerModel.endDate?.before(Date())!!) {
            bindingResult.addError(FieldError("offerModel", "endDate"
                    , "Время окончания акции находится раньше текущего времени"))
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
        } else {
            offerRepository.save(offerModel)
            timerService.runTimer()
            modelMap.addAttribute("status", 200)
            modelMap.addAttribute("message", "Акция успешно создана")
        }
        return ModelAndView("offer_dialog_add", "offerModel", offerModel)
                .addObject("cities", cityRepository.findAll())
    }

    @GetMapping("/offer_dialog")
    @Secured("ROLE_ADMIN")
    fun showAddingOffer(): ModelAndView {
        return ModelAndView("offer_dialog_add", "offerModel", OfferModel())
                .addObject("cities", cityRepository.findAll())
    }

//    @GetMapping("/main_form")
//    fun showForm(): ModelAndView {
//        return ModelAndView("main_form", "offers", offerRepository.findAllByOrderByEndDateDesc())
//                .addObject("offerRequest", OfferRequest(0L, null))
//                .addObject("cities", cityRepository.findAll())
//    }

    @GetMapping("/main_form")
    @Secured("ROLE_ADMIN")
    fun getAdminOffers(offerRequest: OfferRequest?): ModelAndView {
        val offers: List<OfferModel>
        if (offerRequest != null) {
            if (offerRequest.cityId != null && offerRequest.cityId != 0L && offerRequest.active != null)
                offers = offerRepository.findAllByCityAndActiveOrderByEndDateAsc(CityModel(offerRequest.cityId), offerRequest.active)
            else if (offerRequest.cityId != null && offerRequest.cityId != 0L)
                offers = offerRepository.findAllByCityOrderByEndDateAsc(CityModel(offerRequest.cityId))
            else if (offerRequest.active != null)
                offers = offerRepository.findAllByActiveOrderByEndDateAsc(offerRequest.active)
            else offers = offerRepository.findAllByOrderByEndDateDesc()
        } else offers = offerRepository.findAllByOrderByEndDateDesc()
        return ModelAndView("main_form", "offers", offers)
                .addObject("cities", cityRepository.findAll())
    }

    /*    @GetMapping("/main_form")
    fun getAdminOffers(@RequestParam("active")active: Boolean?
                       , @RequestParam("cityId") cityId: Long?): ModelAndView {
        val offers: List<OfferModel>
        if (cityId != null && cityId != 0L && active != null && active)
            offers = offerRepository.findAllByCityAndActiveOrderByEndDateAsc(CityModel(cityId), active)
        else if (cityId != null && cityId != 0L)
            offers = offerRepository.findAllByCityOrderByEndDateAsc(CityModel(cityId))
        else if (active != null && active)
            offers = offerRepository.findAllByActiveOrderByEndDateAsc(active)
        else offers = offerRepository.findAllByOrderByEndDateDesc()
        return ModelAndView("main_form", "offers", offers)

                .addObject("cities", cityRepository.findAll())
    }*/

    @PostMapping("/get_list")
    fun getOffers(@RequestBody offerRequest: OfferRequest?): BaseModel<List<FrontOfferDTO>> {
        val userOffers = ArrayList<OfferModel>()
        if (offerRequest?.cityId == null || offerRequest.cityId == 0L)
            userOffers.addAll(offerRepository.findAllByActiveOrderByEndDateAsc(true))
        else userOffers.addAll(offerRepository.findAllByActiveAndCityOrderByEndDateAsc(true
                , CityModel(offerRequest.cityId)))
        val offers = ArrayList<FrontOfferDTO>()
        userOffers.forEach {
            offers.add(FrontOfferDTO(it))
        }
        return BaseModel(200, "", offers)
    }

}