package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.HistoryRepository
import me.flashka.web.taxi.repository.OfferRepository
import me.flashka.web.taxi.repository.ParticipantRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import me.flashka.web.taxi.repository.enums.WinnerStatus
import me.flashka.web.taxi.repository.model.HistoryModel
import me.flashka.web.taxi.util.RandomCollection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


@Service
class TimerService(
        val offerRepository: OfferRepository,
        val participantRepository: ParticipantRepository,
        val userRepository: UserRepository,
        val historyRepository: HistoryRepository
) {

    var offerIdList: MutableList<Long> = ArrayList()

    val logger: Logger = LoggerFactory.getLogger(TimerService::class.java)

    val runnable: Runnable = Runnable {
        kotlin.run {
            val offers = offerRepository.findAllByActiveOrderByEndDateAsc(true)
            if (offers.isNotEmpty())
                offers.forEach {
                    if (!it.endDate?.after(Date())!!) {
                        selectWinner(it.id)
                        runTimer()
                        offerIdList.remove(it.id)
                        return@Runnable
                    }
                }
        }
    }

    @Async
    fun runTimer() {
        val offers = offerRepository.findAllByActiveOrderByEndDateAsc(true)
        if (offers.isNotEmpty()) {
            val scheduler = ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor())
            offers.forEach {
                if (it.endDate?.after(Date())!! && !offerIdList.contains(it.id)) {
                    offerIdList.add(it.id)
                    scheduler.schedule(runnable, it.endDate)
                    logger.info("endDate is {}", it.endDate)
                    return
                } else if (it.endDate.before(Date()))
                    selectWinner(it.id)
            }
        }
    }

    fun selectWinner(offerId: Long) {
        val offerModel = offerRepository.findById(offerId).get()
        offerModel.active = false

        val userOnlyList = participantRepository.findAllByOffer(offerModel)
        if (userOnlyList.isNotEmpty()) {
            val userModel = RandomCollection().next(userOnlyList)
            val participantModel = participantRepository.findByUserAndOffer(userModel, offerModel)

            offerModel.winnerStatus = WinnerStatus.WINNER_EXISTS

            userModel.balance = userModel.balance + offerModel.win!!
            userModel.weight = 0.5
            userRepository.save(userModel)

            participantModel.winner = true
            participantModel.date = Date()
            participantRepository.save(participantModel)

            historyRepository.save(HistoryModel(userModel, offerModel.win, offerId.toString()
                    , participantModel.id.toString(), 0.0, TransactionStatus.SUCCESSFUL, TransactionType.WINNER))
        } else offerModel.winnerStatus = WinnerStatus.WINNER_DOESNT_EXIST
        offerRepository.save(offerModel)
    }

}