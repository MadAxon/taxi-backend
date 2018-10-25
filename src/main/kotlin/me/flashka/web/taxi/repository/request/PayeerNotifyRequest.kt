package me.flashka.web.taxi.repository.request

import org.apache.commons.codec.digest.DigestUtils
import javax.validation.constraints.NotNull

data class PayeerNotifyRequest(
        val m_shop: String,
        var m_orderid: String?,
        var m_amount: String?,
        val m_curr: String,
        var m_desc: String,
        val key: String = "FAzofhF5GRaXiJAS",
        val m_operation_id: String?,
        val m_operation_ps: String?,
        val m_operation_date: String?,
        val m_operation_pay_date: String?,
        val m_status: String?,
        val m_sign: String? = null
) {


}