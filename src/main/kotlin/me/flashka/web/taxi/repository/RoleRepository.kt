package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.RoleModel
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleModel, Long>