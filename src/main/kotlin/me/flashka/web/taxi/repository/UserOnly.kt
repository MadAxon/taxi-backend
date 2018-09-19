package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.UserModel

interface UserOnly {

    fun getUser(): UserModel

}
