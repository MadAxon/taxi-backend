package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.CityRepository
import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.BaseModel
import org.slf4j.LoggerFactory
import org.springframework.security.access.annotation.Secured
import org.springframework.ui.ModelMap
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.ModelAndView
import java.net.InetAddress
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController()
@RequestMapping(value = ["/city"])
class CityController(private val cityRepository: CityRepository) {

    @GetMapping("/city_form")
    @Secured("ROLE_ADMIN")
    fun showCityForm(): ModelAndView {
        return ModelAndView("city_form", "cities", cityRepository.findAll())
    }

    @GetMapping("/get_list")
    fun getCities(): BaseModel<List<CityModel>> {
        return BaseModel(200, "", cityRepository.findAll())
    }

    @GetMapping("/city_dialog")
    @Secured("ROLE_ADMIN")
    fun showAddingDialog(): ModelAndView {
        return ModelAndView("city_dialog_add", "cityModel", CityModel())
    }

    @PostMapping("/set")
    @Secured("ROLE_ADMIN")
    fun setCity(@Valid @ModelAttribute("cityModel") cityModel: CityModel
                , bindingResult: BindingResult, modelMap: ModelMap): ModelAndView {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
        else if (cityRepository.existsByName(cityModel.name)) {
            bindingResult.addError(FieldError("cityModel", "name", "Город уже существует"))
            modelMap.addAttribute("error", bindingResult.fieldErrors[0].defaultMessage!!)
        } else {
            modelMap.addAttribute("message", "Город успешно добавлен")
            modelMap.addAttribute("status", 200)
            cityRepository.save(cityModel)
        }
        return ModelAndView("city_dialog_add", "cityModel", cityModel)
    }

}