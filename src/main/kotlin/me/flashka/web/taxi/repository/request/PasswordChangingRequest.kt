package me.flashka.web.taxi.repository.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class PasswordChangingRequest(

        @field:NotNull(message = "Укажите текущий пароль")
        val oldPassword: String? = null,

        @field:NotNull(message = "Укажите новый пароль")
        @field:Size(min = 6, message = "Новый пароль должен содержать минимум 6 символов")
        val newPassword: String? = null,

        @field:NotNull(message = "Укажите новый пароль еще раз")
        val newPasswordAgain: String? = null
)