package me.flashka.web.taxi.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import me.flashka.web.taxi.repository.enums.TransactionStatus
import me.flashka.web.taxi.repository.enums.TransactionType
import me.flashka.web.taxi.repository.model.HistoryModel
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated

data class FrontHistoryDTO (

        @JsonIgnore
        val historyModel: HistoryModel,
        val amount: Double? = historyModel.amount,
        val name: String? = historyModel.name,
        val commission: Double = historyModel.commission,
        val date: Date = historyModel.date,

        @Enumerated(EnumType.STRING)
        val transactionStatus: TransactionStatus? = historyModel.transactionStatus,

        @Enumerated(EnumType.STRING)
        val transactionType: TransactionType? = historyModel.transactionType
) {

    var number: String? = null
    get() {
        var string = historyModel.number
        if (transactionType?.equals(TransactionType.PAYOUT)!!) {
            if (name?.contains("+")!!)
                string = string?.replace("^?[9][\\d]{7}?", "*******")
            else string = string?.replace("^?[\\d]{12}?", "************")
        }
        return string
    }

}