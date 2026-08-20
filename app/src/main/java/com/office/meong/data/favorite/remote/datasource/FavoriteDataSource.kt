package com.office.meong.data.favorite.remote.datasource

import com.office.meong.core.network.model.ApiException
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.favorite.remote.api.FavoriteService
import com.office.meong.data.favorite.remote.dto.response.FavoriteResponse
import javax.inject.Inject

class FavoriteDataSource @Inject constructor(
    private val favoriteService: FavoriteService
) {
    suspend fun getFavorites(region: String?, placeType: String?): List<FavoriteResponse> =
        favoriteService.getFavorites(region, placeType).getOrThrow()

    suspend fun postFavorite(placeId: Long): FavoriteResponse =
        favoriteService.postFavorite(placeId).getOrThrow()

    suspend fun deleteFavorite(placeId: Long) {
        val response = favoriteService.deleteFavorite(placeId)
        if (!response.success) throw ApiException(response.message)
    }
}
