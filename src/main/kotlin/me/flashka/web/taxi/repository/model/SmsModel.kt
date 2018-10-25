package me.flashka.web.taxi.repository.model

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
data class SmsModel (
        @Id
        @GeneratedValue
        val id: Long = 0,

        val date: Date = Date(),

        @OneToOne
        @JoinColumn(name = "userId")
        val user: UserModel? = null,

        val code: String? = null
) {
    constructor(user: UserModel?, code: String?) : this(0, Date(), user, code)
}