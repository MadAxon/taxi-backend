package me.flashka.web.taxi.repository.request

data class SmsRequest (
        val login: String = "z1540473831306",
        val password: String = "328260",
        val statusQueueName: String = "hpn",
        val messages: MutableList<SmsInfo> = ArrayList<SmsInfo>()
) {
    constructor(clientId: String, phone: String, text: String) : this() {
        messages.add(SmsInfo(clientId, phone, text))
    }

    data class SmsInfo(
            val clientId: String,
            val phone: String,
            val text: String
    )
}

