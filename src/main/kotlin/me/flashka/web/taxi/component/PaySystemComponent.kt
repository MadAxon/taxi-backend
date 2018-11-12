package me.flashka.web.taxi.component

import me.flashka.web.taxi.repository.PaySystemRepository
import me.flashka.web.taxi.repository.model.PaySystemModel
import me.flashka.web.taxi.repository.response.PaySystemsResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import javax.transaction.Transactional

@Component
class PaySystemComponent(
        private val paySystemRepository: PaySystemRepository
) {

    @Scheduled(cron = "0 0 0 * * mon")
    @Transactional
    fun syncPaySystems() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()
        map.add("account", "P1005149994")
        map.add("apiId", "665366280")
        map.add("apiPass", "r8Pjd356mLsl2BeE")
        map.add("action", "getPaySystems")

        val request = HttpEntity<MultiValueMap<String, String>>(map, headers)

        val response = RestTemplate().postForEntity("https://payeer.com/ajax/api/api.php?getPaySystems", request, PaySystemsResponse::class.java)
        if (response.body?.errors?.size == 0) {
            response.body?.list?.forEach {
                paySystemRepository.save(PaySystemModel(it.value))
            }
        }
    }

}