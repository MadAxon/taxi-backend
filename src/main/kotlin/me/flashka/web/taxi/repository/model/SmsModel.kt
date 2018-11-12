package me.flashka.web.taxi.repository.model

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class SmsModel (
        @Id
        @GeneratedValue
        val id: Long = 0,

        val untilExpiredDate: Date = Date(System.currentTimeMillis() + 900000), // 15 min
        val phoneNumber: String? = null,
        val code: String? = null,
        val ip: String? = null
) {
    constructor(phoneNumber: String?, code: String?) : this(0, Date(), phoneNumber, code)

    constructor(phoneNumber: String?, code: String?, ip: String?) : this(0, Date(), phoneNumber, code, ip)
}