package me.flashka.web.taxi.repository.request

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CarNumberChangingRequest (


        @field:NotNull(message = "Укажите Ваш номер машины")
        @field:Pattern(regexp = "^[А-Яа-я][\\s]*[0-9]{3}[\\s]*[А-Яа-я]{2}[\\s]*[0-9]{2,3}\$"
                , message = "Неверный формат номера машины")
        val carNumber: String?,

        @field:NotNull(message = "Укажите пароль")
        @field:Size(min = 1, message = "Укажите пароль")
        val password: String?

) {
    @JsonIgnore
    var _carNumber: String? = carNumber
        get() {
            var string: String? = carNumber
            string = string?.replace("\\s+".toRegex(), "")
            return string?.toUpperCase()
        }
}