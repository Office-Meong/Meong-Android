package com.office.meong.data.favorite.repository

import com.office.meong.core.model.region.Region
import com.office.meong.data.favorite.model.FavoriteModel

interface FavoriteRepository {
    suspend fun getFavorites(region: Region? = null, placeType: String? = null): Result<List<FavoriteModel>>

    suspend fun addFavorite(placeId: Long): Result<FavoriteModel>

    suspend fun removeFavorite(placeId: Long): Result<Unit>
}
