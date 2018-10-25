package me.flashka.web.taxi.service

import me.flashka.web.taxi.repository.model.UserModel

interface UserService {

    fun save(user: UserModel): String
    fun findByPhoneNumber(phoneNumber: String?): UserModel?

}