package me.flashka.web.taxi.repository.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
data class RoleModel(
        @Id
        val id: Long = 2,
        val name: String = "USER"
)