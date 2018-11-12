package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.dto.FrontHistoryDTO
import me.flashka.web.taxi.repository.model.HistoryModel
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface HistoryRepository : JpaRepository<HistoryModel, Long> {
    fun findAllByUser(user: UserModel): List<HistoryModel>
}