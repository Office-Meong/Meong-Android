package com.office.meong.data.favorite.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.core.model.region.Region
import com.office.meong.data.favorite.model.FavoriteModel
import com.office.meong.data.favorite.model.toModel
import com.office.meong.data.favorite.remote.datasource.FavoriteDataSource
import com.office.meong.data.favorite.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDataSource: FavoriteDataSource,
) : FavoriteRepository {
    override suspend fun getFavorites(region: Region?, placeType: String?): Result<List<FavoriteModel>> =
        suspendRunCatching {
            val regionParam = region?.takeIf { it != Region.UNKNOWN }?.name
            favoriteDataSource.getFavorites(regionParam, placeType).map { it.toModel() }
        }

    override suspend fun addFavorite(placeId: Long): Result<FavoriteModel> = suspendRunCatching {
        favoriteDataSource.postFavorite(placeId).toModel()
    }

    override suspend fun removeFavorite(placeId: Long): Result<Unit> = suspendRunCatching {
        favoriteDataSource.deleteFavorite(placeId)
    }
}
