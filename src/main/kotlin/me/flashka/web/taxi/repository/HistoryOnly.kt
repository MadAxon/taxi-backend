package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.enums.HistoryStatus
import java.util.*

interface HistoryOnly {

    fun getAmount(): Int?
    fun getName(): String?
    fun getNumber(): String?
    fun getCommission(): Int
    fun getDate(): Date
    fun getHistoryStatus(): HistoryStatus?

}