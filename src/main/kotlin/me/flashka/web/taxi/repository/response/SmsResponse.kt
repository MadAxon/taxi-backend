package me.flashka.web.taxi.repository.response

data class SmsResponse (
        val status: String,
        val messages: List<SmsInfo>
) {


    data class SmsInfo(
          val status: String,
          val smscId: String,
          val clientId: String
    )
}