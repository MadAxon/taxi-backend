package me.flashka.web.taxi.repository.request

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils
import javax.validation.constraints.NotNull

data class PayeerRequest (

        val m_shop: String,

        @field:NotNull(message = "Не указан m_orderid")
        var m_orderid: String? = null,

        @field:NotNull(message = "Не указан m_amount")
        var m_amount: String? = null,

        val m_curr: String,

        @field:NotNull(message = "Не указан m_desc")
        var m_desc: String,
        val key: String

        ) {

    var m_sign: String? = null


    constructor(orderId: String?, amount: String?, desc: String)
            : this("659138305", orderId, amount, "RUB", desc, "FAzofhF5GRaXiJAS") {
        this.m_desc = base64String(desc)
        m_sign = DigestUtils.sha256Hex("$m_shop:$orderId:$amount:$m_curr:$desc:$key")
    }


    fun base64String(desc: String) = String(Base64.encodeBase64(desc.toByteArray()))
}