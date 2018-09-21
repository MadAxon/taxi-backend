package me.flashka.web.taxi.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import me.flashka.web.taxi.repository.enums.WinnerStatus
import me.flashka.web.taxi.repository.model.OfferModel
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class FrontOfferDTO(
        @JsonIgnore
        val offer: OfferModel?,

        val id: Long? = offer?.id,
        val startDate: Date? = offer?.startDate,
        val endDate: Date? = offer?.endDate,
        val win: Int? = offer?.win,
        val payment: Int? = offer?.payment,
        val participants: Int? = offer?.participants,

        @Enumerated(EnumType.STRING)
        val winnerStatus: WinnerStatus? = offer?.winnerStatus
)