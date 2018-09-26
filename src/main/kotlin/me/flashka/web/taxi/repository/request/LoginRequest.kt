package me.flashka.web.taxi.repository.request

import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class LoginRequest (

        @field:Pattern(regexp = "^[+\\s]?[7-8\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})\$"
                , message = "Неправильный формат номера")
        @field:NotNull(message = "Укажите номер телефона")
        val phoneNumber: String? = null,

        @field:NotNull(message = "Укажите пароль")
        @field:Size(min = 1, message = "Укажите пароль")
        val password: String? = null

) {

    var _phoneNumber: String? = phoneNumber
    get() {
        var string: String? = phoneNumber
        string = string?.replace("[^\\d]".toRegex(), "")
        if (string?.length == 11) {
            string = string.substring(0, 1).replace("8", "7") + string.substring(1)
        } else if (string?.length == 10) {
            string = "7$string"
        }

        return string
    }

}
