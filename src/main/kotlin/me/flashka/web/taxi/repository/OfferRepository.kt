package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.OfferModel
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<OfferModel, Long> {

    fun findAllByActiveOrderByEndDateAsc(isActive: Boolean): List<OfferModel>

}