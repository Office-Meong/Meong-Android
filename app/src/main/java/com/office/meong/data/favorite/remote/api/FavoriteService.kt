package com.office.meong.data.favorite.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.favorite.remote.dto.response.FavoriteResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteService {
    @GET("favorites")
    suspend fun getFavorites(
        @Query("region") region: String?,
        @Query("placeType") placeType: String?,
    ): BaseResponse<List<FavoriteResponse>>

    @POST("favorites/{placeId}")
    suspend fun postFavorite(
        @Path("placeId") placeId: Long
    ): BaseResponse<FavoriteResponse>

    @DELETE("favorites/{placeId}")
    suspend fun deleteFavorite(
        @Path("placeId") placeId: Long
    ): BaseResponse<Unit>
}
