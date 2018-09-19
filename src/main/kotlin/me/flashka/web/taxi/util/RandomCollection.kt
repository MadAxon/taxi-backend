package me.flashka.web.taxi.util

import me.flashka.web.taxi.repository.UserOnly
import me.flashka.web.taxi.repository.model.UserModel

/**
 * Collection for weight randomness
 */
class RandomCollection {

    private var total: Double = 0.0

    fun next(usersOnly: List<UserOnly>): UserModel {
        total = 0.0
        usersOnly.forEach {
            total += it.getUser().weight
        }
        val random = Math.random() * total

        total = 0.0
        usersOnly.forEach {
            total += it.getUser().weight
            if (total >= random) return it.getUser()
        }

        throw RuntimeException("Exception was thrown by hand in util.RandomCollection.kt")
    }

}