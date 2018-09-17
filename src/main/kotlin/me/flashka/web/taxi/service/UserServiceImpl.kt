package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.RoleRepository
import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*
import org.apache.commons.lang3.RandomStringUtils
import java.security.SecureRandom


@Service
class UserServiceImpl(
        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : UserService {
    override fun save(user: UserModel): Boolean {
        if (!userRepository.existsByPhoneNumber(user._phoneNumber)) {
            user.phoneNumber = user._phoneNumber
            user.password = bCryptPasswordEncoder.encode(generatePassword())
            user.role = roleRepository.findById(2).get()
            userRepository.save(user)
            return true
        }
        return false
    }

    override fun findByPhoneNumber(phoneNumber: String?): UserModel? {
        return userRepository.findByPhoneNumber(phoneNumber)
    }

    private fun generatePassword(): String {
        val possibleCharacters = StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
                .toString().toCharArray()
        val generatedPassword = RandomStringUtils.random(6, 0, possibleCharacters.size - 1
                , false
                , false
                , possibleCharacters
                , SecureRandom())

        return generatedPassword
    }

}