package me.flashka.web.taxi.repository.response

import me.flashka.web.taxi.repository.model.PayeerPaySystemModel
import java.util.*

data class PaySystemsResponse (
        val errors: Array<String>,
        val list: Map<String, PayeerPaySystemModel>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaySystemsResponse

        if (!Arrays.equals(errors, other.errors)) return false
        if (list != other.list) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(errors)
        result = 31 * result + list.hashCode()
        return result
    }


}