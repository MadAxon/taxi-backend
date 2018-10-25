package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.PaySystemModel
import org.springframework.data.jpa.repository.JpaRepository

interface PaySystemRepository : JpaRepository<PaySystemModel, Long> {
}