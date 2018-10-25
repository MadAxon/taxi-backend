package me.flashka.web.taxi.repository.model

import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class HistoryModel(

        @Id
        @GeneratedValue
        val id: Long = 0,

        @OneToOne
        @JoinColumn(name = "userId")
        @field:NotNull(message = "Операция не может быть совершена без указания пользователя")
        val user: UserModel? = null,

        @field:NotNull(message = "Укажите сумму оплаты/вывода/выигрыша пользователя")
        val amount: Double? = null,
        val name: String? = null,
        val number: String? = null,
        val commission: Double = 0.0,
        val date: Date = Date(),

        @Enumerated(EnumType.STRING)
        @field:NotNull(message = "Не указан статус операции")
        val transactionStatus: TransactionStatus? = null,

        @Enumerated(EnumType.STRING)
        @field:NotNull(message = "Не указан тип операции")
        val transactionType: TransactionType? = null

) {
    constructor(user: UserModel?, amount: Double?, name: String?, number: String?, commission: Double, transactionStatus: TransactionStatus?
                , transactionType: TransactionType?)
            : this(0, user, amount, name, number, commission, Date(), transactionStatus, transactionType)
}