package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.SmsModel
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface SmsRepository : JpaRepository<SmsModel, Long> {

    fun findByUserAndCode(user: UserModel, code: String): SmsModel
    fun existsByUserAndCode(user: UserModel, code: String): Boolean
    fun deleteByUserAndCode(user: UserModel, code: String)
}