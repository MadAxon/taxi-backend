package me.flashka.web.taxi.repository.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class PayeerPayoutRequest (

        @field:NotNull(message = "Укажите платежную систему")
        val systemId: Long? = null,

        @field:NotNull(message = "Укажите сумму вывода")
        @field:Min(value = 1, message = "Сумма вывода не может быть менее 1 руб")
        val amount: Double? = null,

        @field:NotNull(message = "Укажите номер счета получателя")
        val accountNumber: String? = null,

        val code: String?
)