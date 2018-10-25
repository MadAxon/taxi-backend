package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserModel, Long> {

    fun findByPhoneNumber(phoneNumber: String?): UserModel?
    fun existsByPhoneNumber(phoneNumber: String?): Boolean
    fun existsByCarNumber(carNumber: String?): Boolean

}
