package com.office.meong.domain.favorite.usecase

import com.office.meong.data.favorite.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) {
    suspend fun toggle(placeId: Long, isFavorite: Boolean): Result<Unit> =
        if (isFavorite) {
            favoriteRepository.removeFavorite(placeId)
        } else {
            favoriteRepository.addFavorite(placeId).map { }
        }
}
