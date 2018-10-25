package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.RoleRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*
import org.apache.commons.lang3.RandomStringUtils
import org.apache.logging.log4j.LogManager
import java.security.SecureRandom


@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {

    val logger = LogManager.getLogger(UserServiceImpl::class)

    override fun save(user: UserModel): String {
        user.phoneNumber = user._phoneNumber
        user.carNumber = user._carNumber
        val generatedPassword = generatePassword()
        user.password = bCryptPasswordEncoder.encode(generatedPassword)
        user.role = roleRepository.findById(2).get()
        userRepository.save(user)
        return generatedPassword
    }

    override fun findByPhoneNumber(phoneNumber: String?): UserModel? {
        return userRepository.findByPhoneNumber(phoneNumber)
    }

    private fun generatePassword(): String {
        val possibleCharacters = StringBuilder("0123456789")
                .toString().toCharArray()
        return RandomStringUtils.random(6, 0, possibleCharacters.size - 1
                , false
                , false
                , possibleCharacters
                , SecureRandom())
    }

}