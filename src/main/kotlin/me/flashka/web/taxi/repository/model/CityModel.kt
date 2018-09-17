package me.flashka.web.taxi.repository.model

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
data class CityModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = ""

)