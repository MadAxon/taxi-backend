package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import kotlin.collections.HashSet

@Entity
data class UserModel(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:Pattern(regexp = "^[+\\s]?[7-8\\s]?\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})\$"
                , message = "Неправильный формат номера")
        @field:NotNull(message = "Укажите номер телефона")
        var phoneNumber: String?,

        @field:NotNull(message = "Укажите Ваше имя")
        var firstName: String?,

        @field:NotNull(message = "Укажите Вашу фамилию")
        var lastName: String?,
        var patronymic: String? = "",

        @ManyToOne
        @JoinColumn(name = "cityId")
        @field:NotNull(message = "Укажите город")
        var city: CityModel? = null,

        @JsonFormat(pattern = "dd.MM.yyyy")
        val birthDate: Date? = null,

        @field:NotNull(message = "Укажите Ваш номер машины")
        @field:Pattern(regexp = "^[А-Яа-я][\\s]*[0-9]{3}[\\s]*[А-Яа-я]{2}[\\s]*[0-9]{2,3}\$"
                , message = "Неверный формат номера машины")
        var carNumber: String?,

        @JsonIgnore
        @JsonSetter
        var password: String = "",

        @ManyToOne
        @JoinColumn(name = "roleId")
        @JsonIgnore
        var role: RoleModel = RoleModel(),

        val registerDate: Date = Date(),

        @JsonIgnore
        var weight: Double = 1.0,

        var balance: Double = 0.0) {

    constructor() : this(0, "", "", "", ""
            , CityModel(0, ""), Date(), "", "")

    @Transient
    @JsonIgnore
    var _phoneNumber: String? = phoneNumber
        get() {
            var string: String? = phoneNumber
            string = string?.replace("[^\\d]".toRegex(), "")
            if (string?.length == 11) {
                string = string.substring(0, 1).replace("8", "7") + string.substring(1)
            } else if (string?.length == 10) {
                string = "7$string"
            }

            return string
        }

    @Transient
    @JsonIgnore
    var _carNumber: String? = carNumber
    get() {
        var string: String? = carNumber
        string = string?.replace("\\s+".toRegex(), "")
        return string?.toUpperCase()
    }

}