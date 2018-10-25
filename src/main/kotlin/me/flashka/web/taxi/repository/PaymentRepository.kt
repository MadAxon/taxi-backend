package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.PaymentModel
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<PaymentModel, Long> {
}