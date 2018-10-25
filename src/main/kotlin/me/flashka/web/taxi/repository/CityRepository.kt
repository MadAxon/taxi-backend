package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.CityModel
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<CityModel, Long> {

    fun existsByName(name: String?): Boolean

}