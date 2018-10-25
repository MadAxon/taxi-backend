package me.flashka.web.taxi.repository

import me.flashka.web.taxi.repository.model.OfferModel
import me.flashka.web.taxi.repository.model.ParticipantModel
import me.flashka.web.taxi.repository.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.lang.Nullable

interface ParticipantRepository : JpaRepository<ParticipantModel, Long> {

    fun findAllByOfferOrderByDateDesc(offer: OfferModel): List<ParticipantModel>

    fun findAllByOffer(offer: OfferModel): List<UserOnly>

    fun findByUserAndOffer(user: UserModel, offer: OfferModel): ParticipantModel

    fun existsByUserAndOffer(user: UserModel, offer: OfferModel): Boolean

    fun findAllByWinner(boolean: Boolean): List<ParticipantModel>

}