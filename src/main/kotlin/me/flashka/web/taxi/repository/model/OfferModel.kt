package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
data class OfferModel(

        @Id
        @GeneratedValue
        val id: Long = 0,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "GMT+03:00")
        @field:NotNull(message = "Укажите дату и время начала акции")
        val startDate: Date?,

        @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "GMT+03:00")
        @field:NotNull(message = "Укажите дату и время конца акции")
        val endDate: Date?,

        @field:NotNull(message = "Укажите сумму выигрыша")
        @field:Min(0, message = "Сумма выигрыша не может быть отрицательной")
        val win: Int?,

        @field:NotNull(message = "Укажите сумму участия в акции")
        @field:Min(0, message = "Сумма участия не может быть отрицательной")
        val payment: Int?,

        var active: Boolean = true

) {
        constructor() : this(0 ,null, null, 0, 0)
}