package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.HistoryRepository
import me.flashka.web.taxi.repository.PaySystemRepository
import me.flashka.web.taxi.repository.SmsRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import me.flashka.web.taxi.repository.model.BaseModel
import me.flashka.web.taxi.repository.model.HistoryModel
import me.flashka.web.taxi.repository.model.PaySystemModel
import me.flashka.web.taxi.repository.model.SmsModel
import me.flashka.web.taxi.repository.request.PayeerPayoutRequest
import me.flashka.web.taxi.repository.request.SmsRequest
import me.flashka.web.taxi.repository.response.PayoutResponse
import me.flashka.web.taxi.repository.response.SmsResponse
import me.flashka.web.taxi.service.SmsCleanerService
import org.apache.commons.lang3.RandomStringUtils
import org.slf4j.LoggerFactory
import org.springframework.util.MultiValueMap
import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.security.SecureRandom
import java.util.*
import javax.validation.Valid


@RestController
class PayoutController(
        private val userRepository: UserRepository,
        private val tokenProvider: JwtTokenProvider,
        private val historyRepository: HistoryRepository,
        private val paySystemRepository: PaySystemRepository,
        private val smsRepository: SmsRepository,
        private val smsCleanerService: SmsCleanerService
) {

    private val logger = LoggerFactory.getLogger(PayoutController::class.java)

    @GetMapping("/payout/pay_systems/get_list")
    fun getPaySystems(): BaseModel<List<PaySystemModel>> {
        return BaseModel(200, "", paySystemRepository.findAllByActive(true))
    }

    @PostMapping("/payout")
    fun payout(@RequestHeader("Authorization") token: String,
               @Valid @RequestBody payeerPayoutRequest: PayeerPayoutRequest,
               bindingResult: BindingResult): BaseModel<Any> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
                ?: return BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        if (userModel.balance < payeerPayoutRequest.amount!!)
            return BaseModel(400, "Недостаточно средств в балансе кошелька для вывода средств")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val map = LinkedMultiValueMap<String, String>()
        map.add("account", "P1005149994")
        map.add("apiId", "665366280")
        map.add("apiPass", "r8Pjd356mLsl2BeE")
        map.add("action", "initOutput")
        map.add("ps", payeerPayoutRequest.systemId.toString())
        map.add("sumOut", payeerPayoutRequest.amount.toString())
        map.add("curIn", "RUB")
        map.add("curOut", "RUB")
        map.add("param_ACCOUNT_NUMBER", payeerPayoutRequest.accountNumber)

        val request = HttpEntity<MultiValueMap<String, String>>(map, headers)

        val responseInitOutput = RestTemplate().postForEntity("https://payeer.com/ajax/api/api.php?initOutput", request, PayoutResponse::class.java)
        if (userModel.balance < responseInitOutput.body?.outputParams?.sumIn!!) {
            return BaseModel(400, "Недостаточно средств в балансе кошелька для вывода средств")
        }
        var paySystemModel = paySystemRepository.findById(responseInitOutput.body?.outputParams?.ps!!).get()
        if (paySystemModel.sumMinRub!! > payeerPayoutRequest.amount) {
            return BaseModel(400, "Минимальная возможная сумма вывода - " + paySystemModel.sumMinRub)
        }
        if (paySystemModel.sumMaxRub!! < payeerPayoutRequest.amount) {
            return BaseModel(400, "Максимальная возможная сумма вывода - " + paySystemModel.sumMaxRub)
        }
        if (responseInitOutput.body?.errors != null) {
            logger.error(responseInitOutput.body?.errors.toString())
            return BaseModel(400, "Неполадки в платежном шлюзе. Повторите попытку позже")
        }

        if (smsRepository.existsByPhoneNumberAndUntilExpiredDateBefore(userModel.phoneNumber, Date())) {
            return BaseModel(400, "Sms сообщение уже отправлено на ваш номер телефона")
        }

        if (payeerPayoutRequest.code.isNullOrBlank()) {
            val code = generateCode()
            if (sendSms(userModel.id, userModel.phoneNumber!!, code) == "accepted") {
                smsRepository.save(SmsModel(userModel.phoneNumber, code))
                smsCleanerService.deleteEntityFromTableWithDelay(userModel, code)
                return BaseModel(201, "На ваш номер телефона выслано sms сообщение с кодом")
            }
            return BaseModel(400, "Неполадки в sms сервисе. Повторите попытку позже")
        }

        if (!smsRepository.existsByPhoneNumberAndCode(userModel.phoneNumber, payeerPayoutRequest.code!!))
            return BaseModel(400, "Указан неверный код!")

        map.remove("action")
        map.add("action", "output")
        val responseOutput = RestTemplate().postForEntity("https://payeer.com/ajax/api/api.php?output", request, PayoutResponse::class.java)
        if (responseOutput.body?.historyId != null) {
            userModel.balance = userModel.balance - payeerPayoutRequest.amount
            paySystemModel = paySystemRepository.findById(responseOutput.body?.outputParams?.ps!!).get()
            historyRepository.save(HistoryModel(userModel,
                    payeerPayoutRequest.amount,
                    paySystemModel.name,
                    payeerPayoutRequest.accountNumber,
                    responseOutput.body?.outputParams?.sumIn!! - responseOutput.body?.outputParams?.sumOut!!,
                    TransactionStatus.SUCCESSFUL,
                    TransactionType.PAYOUT))
            return BaseModel(200, "Вывод успешно завершен")
        } else {
            logger.error(responseInitOutput.body?.errors.toString())
            return BaseModel(400, "Неполадки в платежном шлюзе. Попробуйте повторить позже")
        }
    }

    fun sendSms(userId: Long, phone: String, code: String): String {
        val restTemplate = RestTemplate()
        val request = HttpEntity(SmsRequest(userId.toString(), phone, code))
        val smsResponse = restTemplate.postForObject(
                "https://api.smsbliss.net/messages/v2/send.json",
                request,
                SmsResponse::class.java)
        if (smsResponse?.messages?.size!! > 0) {
            return smsResponse.messages[0].status
        }
        return ""
    }

    fun generateCode(): String {
        val possibleCharacters = StringBuilder("0123456789")
                .toString().toCharArray()
        return RandomStringUtils.random(4, 0, possibleCharacters.size - 1
                , false
                , false
                , possibleCharacters, null)

    }

}