package me.flashka.web.taxi.repository.request

import javax.validation.constraints.NotNull

data class PayeerPayoutRequest (

        @field:NotNull(message = "Укажите платежную систему")
        val systemId: String? = null,

        @field:NotNull(message = "Укажите сумму вывода")
        val amount: Double? = null,

        @field:NotNull(message = "Укажите номер счета получателя")
        val accountNumber: String? = null,

        val code: String?
)