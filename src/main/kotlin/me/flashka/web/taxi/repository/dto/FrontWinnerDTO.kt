package me.flashka.web.taxi.repository.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import me.flashka.web.taxi.repository.model.ParticipantModel

data class FrontWinnerDTO(
        @JsonIgnore
        private val participantModel: ParticipantModel,

        val offer: FrontOfferDTO = FrontOfferDTO(participantModel.offer),

        val winner: FrontUserDTO = FrontUserDTO(participantModel.user?.firstName
                , participantModel.user?.lastName
                , participantModel.user?.patronymic
                , participantModel.user?.carNumber)
)
