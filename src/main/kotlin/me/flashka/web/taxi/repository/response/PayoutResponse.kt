package me.flashka.web.taxi.repository.response

data class PayoutResponse (
        val historyId: Int?,
        val errors: kotlin.Any?,
        val outputParams: OutputParams
) {
    data class OutputParams(
            val sumIn: Double,
            val curIn: String,
            val curOut: String,
            val ps: Long,
            val sumOut: Double
    )
}