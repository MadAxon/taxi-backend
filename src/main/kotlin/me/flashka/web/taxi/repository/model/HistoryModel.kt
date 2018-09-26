package me.flashka.web.taxi.repository.model

import me.flashka.web.taxi.repository.enums.HistoryStatus
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
        val amount: Int? = null,
        val name: String? = null,
        val number: String? = null,
        val commission: Int = 0,
        val date: Date = Date(),

        @Enumerated(EnumType.STRING)
        @field:NotNull(message = "Укажите статус операции")
        val historyStatus: HistoryStatus? = null
) {
    constructor(user: UserModel?, amount: Int?, name: String?, number: String?, historyStatus: HistoryStatus?)
            : this(0, user, amount, name, number, 0, Date(), historyStatus)
}