package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.CityRepository
import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.BaseModel
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController()
@RequestMapping(value = ["/city"])
class CityController(private val cityRepository: CityRepository) {

    @GetMapping("/get_list")
    fun getCities(): BaseModel<List<CityModel>> {
        return BaseModel(200, "", cityRepository.findAll())
    }

    @PostMapping("/set")
    fun setCity(@Valid @RequestBody cityModel: CityModel, bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        cityRepository.save(cityModel)
        return BaseModel(200, "Город добавлен")
    }

}