package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class ParticipantModel(

        @Id
        @GeneratedValue
        val id: Long = 0,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
        @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
        val date: Date = Date(),

        @OneToOne
        @JoinColumn(name = "userId")
        @field:NotNull(message = "Не указан участник акции")
        val user: UserModel? = null,

        @OneToOne
        @JoinColumn(name = "offerId")
        @field:NotNull(message = "Не указана акция участника")
        val offer: OfferModel? = null,

        var winner: Boolean = false

)