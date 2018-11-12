package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

data class PayeerPaySystemModel(
    val id: String,
    val name: String,

    val gate_commission: kotlin.Any,
    val currencies: Array<String>,
    val commission_site_percent: Double,
    val sum_min: PayeerGateModel,
    val sum_max: PayeerGateModel,
    val r_fields: AccountNumber
) {

    val gateCommission: kotlin.Any?
    get() {
        if (gate_commission !is ArrayList<*>)
            return gate_commission as LinkedHashMap<*, *>
        return LinkedHashMap<String, String>()
    }

    data class PayeerGateModel(
            @JsonProperty("USD")
            val USD: Double?,

            @JsonProperty("RUB")
            val RUB: Double?,

            @JsonProperty("EUR")
            val EUR: Double?
    )

    data class AccountNumber (

            @JsonProperty("ACCOUNT_NUMBER")
            val ACCOUNT_NUMBER: PayeerAccountNumberModel
    )

    data class PayeerAccountNumberModel (
            val name: String,
            val reg_expr: String,
            val example: String
    )

}