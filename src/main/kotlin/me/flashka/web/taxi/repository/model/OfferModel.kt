package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import me.flashka.web.taxi.repository.enums.WinnerStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
data class OfferModel(

        @Id
        @GeneratedValue
        val id: Long = 0,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "GMT+03:00")
        @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
        @field:NotNull(message = "Укажите дату и время начала акции")
        val startDate: Date? = null,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "GMT+03:00")
        @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
        @field:NotNull(message = "Укажите дату и время конца акции")
        val endDate: Date? = null,

        @field:NotNull(message = "Укажите сумму выигрыша")
        @field:Min(0, message = "Сумма выигрыша не может быть отрицательной")
        val win: Double? = null,

        @field:NotNull(message = "Укажите сумму участия в акции")
        @field:Min(0, message = "Сумма участия не может быть отрицательной")
        val payment: Double? = null,

        @ManyToOne
        @JoinColumn(name = "cityId")
        @field:NotNull(message = "Укажите город проведения акции")
        val city: CityModel? = null,

        @Enumerated(EnumType.STRING)
        var winnerStatus: WinnerStatus = WinnerStatus.WINNER_PENDING,

        var active: Boolean = true,
        var participants: Int = 0
) {}