package me.flashka.web.taxi.repository.model

import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import javax.persistence.*
import javax.validation.constraints.NotNull
@Entity
data class PaymentModel (
        @Id
        @GeneratedValue
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "userId")
        val user: UserModel?,

        @ManyToOne
        @JoinColumn(name = "offerId")
        val offer: OfferModel?
) {

        constructor(user: UserModel?, offer: OfferModel?) : this(0, user, offer)

}