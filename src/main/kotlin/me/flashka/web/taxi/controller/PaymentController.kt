package me.flashka.web.taxi.controller

import me.flashka.web.taxi.jwt.JwtTokenProvider
import me.flashka.web.taxi.repository.*
import me.flashka.web.taxi.repository.enums.PaymentType
import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import me.flashka.web.taxi.repository.model.*
import me.flashka.web.taxi.repository.request.PayeerRequest
import me.flashka.web.taxi.repository.request.PaymentRequest
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid
import java.text.DecimalFormat
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping(value = ["/payment"])
class PaymentController(
        private val participantRepository: ParticipantRepository,
        private val offerRepository: OfferRepository,
        private val userRepository: UserRepository,
        private val tokenProvider: JwtTokenProvider,
        private val historyRepository: HistoryRepository,
        private val paymentRepository: PaymentRepository
) {

    private val logger = LoggerFactory.getLogger(PaymentController::class.java)

    @PostMapping("/apply")
    fun payWithBalance(@RequestHeader("Authorization") token: String,
                       @Valid @RequestBody paymentRequest: PaymentRequest,
                       bindingResult: BindingResult): BaseModel<String> {
        if (bindingResult.hasErrors() && bindingResult.fieldErrors[0].defaultMessage != null)
            return BaseModel(400, bindingResult.fieldErrors[0].defaultMessage!!)
        val phoneNumber = tokenProvider.phoneNumberFromJWT(token.substring(7))
        val userModel = userRepository.findByPhoneNumber(phoneNumber)
                ?: return BaseModel(400, "Не удалось заполучить профиль пользователя. Возможно, он был удален")
        val offerModel = offerRepository.findById(paymentRequest.offerId).get()
        if (offerModel.startDate?.after(Date())!!)
            return BaseModel(400, "Акция не началась. Регистрация невозможна")
        if (!offerModel.active)
            return BaseModel(400, "Акция закончена. Регистрация невозможна")
        if (participantRepository.existsByUserAndOffer(userModel, offerModel))
            return BaseModel(400, "Вы уже зарегистрированы в акции")
        val participantModel = ParticipantModel(offerModel, userModel)
        when (paymentRequest.paymentType) {
            PaymentType.BALANCE -> {
                if (userModel.balance >= offerModel.payment!!) {
                    userModel.balance = userModel.balance - offerModel.payment
                    return registerParticipant(participantModel)
                } else return BaseModel(400, "Недостаточно средств на балансе")
            }
            PaymentType.VISA -> {
                if (offerModel.payment == 0.0) return registerParticipant(participantModel)
                else return BaseModel(200,
                        "",
                        generatePayeerUrl(participantModel))
            }
            else -> {
                if (offerModel.payment == 0.0) return registerParticipant(participantModel)
                else return BaseModel(100, "Произведите оплату для участия в акции")
            }
        }

    }

    @PostMapping("/notify")
    fun notify(@RequestParam body: Map<String, String>, request: HttpServletRequest): BaseModel<Any> {
        val ip = request.remoteAddr
        logger.error("-----------------------------------------NOTIFY------------------------------------")
        logger.error("ip = $ip")
        if (ip == "185.71.65.92" || ip == "185.71.65.189" || ip == "149.202.17.210") {
            val m_operation_id = body["m_operation_id"]
            val m_operation_ps = body["m_operation_ps"]
            val m_operation_date = body["m_operation_date"]
            val m_operation_pay_date = body["m_operation_pay_date"]
            val m_shop = body["m_shop"]
            val m_orderid = body["m_orderid"]
            val m_amount = body["m_amount"]
            val m_curr = body["m_curr"]
            val m_desc = body["m_desc"]
            val m_status = body["m_status"]
            val m_sign = body["m_sign"]
            val summa_out = body["summa_out"]
            val key = "FAzofhF5GRaXiJAS"

            logger.error("m_operation_id = $m_operation_id")
            logger.error("m_operation_ps = $m_operation_ps")
            logger.error("m_operation_date = $m_operation_date")
            logger.error("m_operation_pay_date = $m_operation_pay_date")
            logger.error("m_shop = $m_shop")
            logger.error("m_orderid = $m_orderid")
            logger.error("m_amount = $m_amount")
            logger.error("m_curr = $m_curr")
            logger.error("m_desc = $m_desc")
            logger.error("m_status = $m_status")
            logger.error("m_sign = $m_sign")
            logger.error("summa_out = $summa_out")

            val m_sign_hash: String? = DigestUtils.sha256Hex("$m_operation_id:$m_operation_ps:$m_operation_date:$m_operation_pay_date:$m_shop:$m_orderid:$m_amount:$m_curr:$m_desc:$m_status:$key")

            if (body["m_operation_id"]?.isNotEmpty()!! && m_sign?.isNotEmpty()!!) {
                val paymentModel = paymentRepository.findById(m_orderid?.toLong()!!).get()
                if (m_sign == m_sign_hash
                        && m_status == "success") {
                    historyRepository.save(HistoryModel(
                            paymentModel.user,
                            m_amount?.toDouble(),
                            m_operation_ps,
                            m_operation_id,
                            if (summa_out != null && m_amount != null) summa_out.toDouble() - m_amount.toDouble() else 0.0,
                            TransactionStatus.SUCCESSFUL,
                            TransactionType.PURCHASE))
                    logger.error("-----------------------------------------200, success------------------------------------")
                    logger.error("-----------------------------------------END------------------------------------")
                    return BaseModel(200, "Оплата успешно завершена")
                }
            }
        }
        logger.error("-----------------------------------------400, denied------------------------------------")
        logger.error("-----------------------------------------END------------------------------------")
        return BaseModel(400, "Payment denied!")
    }

    fun registerParticipant(participantModel: ParticipantModel): BaseModel<String> {
        if (participantModel.user!!.weight < 1)
            participantModel.user.weight += 0.1
        participantRepository.save(participantModel)
        participantModel.offer?.participants = participantModel.offer?.participants!! + 1
        offerRepository.save(participantModel.offer)
        return BaseModel(200, "Вы успешно зарегистрировались в акции")
    }

    fun generatePayeerUrl(participantModel: ParticipantModel): String {
        val paymentModel = paymentRepository.save(PaymentModel(participantModel.user, participantModel.offer))
        val payeerRequest = PayeerRequest(
                paymentModel.id.toString(),
                DecimalFormat("#.00").format(participantModel.offer?.payment),
                "Оплата для участия в акции №${participantModel.offer?.id}")
        return "https://payeer.com/merchant?m_shop=${payeerRequest.m_shop}&m_orderid=${payeerRequest.m_orderid}&m_amount=${payeerRequest.m_amount}&m_curr=${payeerRequest.m_curr}&m_desc=${payeerRequest.m_desc}&m_sign=${payeerRequest.m_sign}"
    }

}