package com.office.meong.data.geocoding.remote.api

import com.office.meong.data.geocoding.remote.dto.response.KakaoCoordToAddressResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalService {
    @GET("v2/local/geo/coord2address.json")
    suspend fun coord2Address(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): KakaoCoordToAddressResponse
}
