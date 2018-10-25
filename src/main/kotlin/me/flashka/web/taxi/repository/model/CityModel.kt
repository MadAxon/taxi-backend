package me.flashka.web.taxi.repository.model

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
data class CityModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotNull(message = "Укажите город")
    @field:Size(min = 1, message = "Укажите город")
    val name: String? = null

)