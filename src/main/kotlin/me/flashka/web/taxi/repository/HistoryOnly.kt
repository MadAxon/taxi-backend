package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import java.util.*

interface HistoryOnly {

    fun getAmount(): Int?
    fun getName(): String?
    fun getNumber(): String?
    fun getCommission(): Int
    fun getDate(): Date
    fun getTransactionStatus(): TransactionStatus?
    fun getTransactionType(): TransactionType?

}