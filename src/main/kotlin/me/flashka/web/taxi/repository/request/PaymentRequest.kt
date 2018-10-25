package me.flashka.web.taxi.repository.request

import me.flashka.web.taxi.repository.enums.PaymentType
import me.flashka.web.taxi.repository.model.OfferModel
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class PaymentRequest (

        @field:Min(1,  message = "Не указана акция участника")
        val offerId: Long = 0,

        @field:NotNull(message = "Не указан paymentType")
        val paymentType: PaymentType?
)