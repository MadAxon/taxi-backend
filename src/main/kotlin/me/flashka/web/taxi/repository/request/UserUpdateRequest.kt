package me.flashka.web.taxi.repository.request

import com.fasterxml.jackson.annotation.JsonFormat
import me.flashka.web.taxi.repository.model.CityModel
import java.util.*
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

data class UserUpdateRequest (

    @field:NotNull(message = "Укажите Ваше имя")
    val firstName: String?,

    @field:NotNull(message = "Укажите Вашу фамилию")
    val lastName: String?,
    val patronymic: String? = "",

    @ManyToOne
    @JoinColumn(name = "cityId")
    @field:NotNull(message = "Укажите город")
    val city: CityModel? = null,

    @JsonFormat(pattern = "dd.MM.yyyy")
    val birthDate: Date? = null

)