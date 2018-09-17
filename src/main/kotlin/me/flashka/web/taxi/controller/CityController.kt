package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.CityRepository
import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.BaseModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CityController(private val cityRepository: CityRepository) {

    @GetMapping("/cities")
    fun getCities(): BaseModel<List<CityModel>> {
        return BaseModel(200, "", cityRepository.findAll())
    }

}