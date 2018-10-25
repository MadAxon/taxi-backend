package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.CityModel
import me.flashka.web.taxi.repository.model.OfferModel
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<OfferModel, Long> {

    fun findAllByOrderByEndDateDesc(): List<OfferModel>
    fun findAllByActiveOrderByEndDateAsc(active: Boolean): List<OfferModel>
    fun findAllByActiveAndCityOrderByEndDateAsc(active: Boolean, city: CityModel): List<OfferModel>
    fun findAllByCityOrderByEndDateAsc(city: CityModel): List<OfferModel>
    fun findAllByCityAndActiveOrderByEndDateAsc(city: CityModel, active: Boolean): List<OfferModel>

}