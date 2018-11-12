package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.SmsRepository
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.Executors

@Service
class SmsCleanerService(
        val smsRepository: SmsRepository
) {

    @Async
    fun deleteEntityFromTableWithDelay(user: UserModel, code: String) {
        val scheduler = ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor())
        scheduler.schedule(SmsCleanerRunnable(user, code, smsRepository),
                smsRepository.findByPhoneNumberAndCode(user.phoneNumber, code).untilExpiredDate)
    }

    data class SmsCleanerRunnable(
            val user: UserModel,
            val code: String,
            val smsRepository: SmsRepository
    ) : Runnable {

        override fun run() {
            smsRepository.deleteByPhoneNumberAndCode(user.phoneNumber, code)
        }

    }

}