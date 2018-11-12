package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.model.PaymentModel
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<PaymentModel, Long> {

    fun findByUserAndOffer(user: UserModel, offer: OfferModel): PaymentModel

}