package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.SmsModel
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface SmsRepository : JpaRepository<SmsModel, Long> {

    fun findByPhoneNumberAndCode(phoneNumber: String?, code: String): SmsModel
    fun existsByPhoneNumberAndCode(phoneNumber: String?, code: String): Boolean
    fun deleteByPhoneNumberAndCode(phoneNumber: String?, code: String)
    fun existsByPhoneNumberAndUntilExpiredDateBefore(phoneNumber: String?, currentDate: Date): Boolean
    fun existsByPhoneNumberAndIp(phoneNumber: String?, ip: String) : Boolean
}