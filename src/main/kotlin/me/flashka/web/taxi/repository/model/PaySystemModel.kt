package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class PaySystemModel (

        @Transient
        @JsonIgnore
        val payeerPaySystemModel: PayeerPaySystemModel? = null,

        @Id
        val id: Long? = payeerPaySystemModel?.id?.toLong(),

        val gateCommissionRub: String? = (payeerPaySystemModel?.gateCommission as? LinkedHashMap<String, String>)?.get("RUB"),
        val name: String? = payeerPaySystemModel?.name,
        var commission: Double? = payeerPaySystemModel?.commission_site_percent,
        val sumMinRub: String? = payeerPaySystemModel?.sum_min?.RUB,
        val sumMaxRub: String? = payeerPaySystemModel?.sum_max?.RUB,

        @JsonIgnore
        @Column(insertable = true, updatable = false)
        val active: Boolean? = payeerPaySystemModel?.currencies?.contains("RUB"),
        val accountName: String? = payeerPaySystemModel?.r_fields?.ACCOUNT_NUMBER?.name,
        val regex: String? = payeerPaySystemModel?.r_fields?.ACCOUNT_NUMBER?.reg_expr,
        val hint: String? = payeerPaySystemModel?.r_fields?.ACCOUNT_NUMBER?.example

)