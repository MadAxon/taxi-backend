package me.flashka.web.taxi.repository.request

import javax.validation.constraints.NotNull

class PasswordChangingRequest(

        @field:NotNull(message = "Укажите текущий пароль")
        val oldPassword: String?,

        @field:NotNull(message = "Укажите новый пароль")
        val newPassword: String?,

        @field:NotNull(message = "Укажите новый пароль еще раз")
        val newPasswordAgain: String?
)