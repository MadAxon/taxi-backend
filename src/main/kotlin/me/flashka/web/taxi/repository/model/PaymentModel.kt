package me.flashka.web.taxi.repository.model

import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import javax.persistence.*
import javax.validation.constraints.NotNull
@Entity
data class PaymentModel (
        @Id
        @GeneratedValue
        val id: Long = 0,

        var mOperationId: String? = null,

        @ManyToOne
        @JoinColumn(name = "userId")
        var user: UserModel? = null,

        var amount: Double? = null,

        var mDesc: String = "",
        var sign: String? = null,

        @Transient
        var transactionType: TransactionType? = TransactionType.PURCHASE

        )