package me.flashka.web.taxi.repository.enums

import java.util.*

enum class PaymentType {
    FREE("free"),
    BALANCE("balance"),
    VISA("visa");

    private var value: String? = null

    private constructor(value: String?) {
        this.value = value
    }
}