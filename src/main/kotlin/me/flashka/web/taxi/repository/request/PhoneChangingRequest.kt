package me.flashka.web.taxi.repository.request

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class PhoneChangingRequest (

        @field:Pattern(regexp = "^[+\\s]?[7-8\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})\$"
                , message = "Неправильный формат номера")
        @field:NotNull(message = "Укажите номер телефона")
        val newPhone: String? = null,

        val code: String? = null
) {
    @JsonIgnore
    var _newPhone: String? = newPhone
        get() {
            var string: String? = newPhone
            string = string?.replace("[^\\d]".toRegex(), "")
            if (string?.length == 11) {
                string = string.substring(0, 1).replace("8", "7") + string.substring(1)
            } else if (string?.length == 10) {
                string = "7$string"
            }

            return string
        }
}