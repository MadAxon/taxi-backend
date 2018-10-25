package me.flashka.web.taxi.repository.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseModel<T>(
        val status: Int = 0,
        val message: String = "",
        val result: T? = null
)