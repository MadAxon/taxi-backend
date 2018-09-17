package me.flashka.web.taxi.controller

import me.flashka.web.taxi.repository.UserRepository
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val repository: UserRepository) {

    @GetMapping("/users")
    fun getUsers(): List<UserModel> = repository.findAll()

}