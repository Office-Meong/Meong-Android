package com.office.meong.data.geocoding.remote.datasource

import com.office.meong.data.geocoding.remote.api.KakaoLocalService
import com.office.meong.data.geocoding.remote.dto.response.KakaoCoordToAddressResponse
import javax.inject.Inject

class GeocodingDataSource @Inject constructor(
    private val kakaoLocalService: KakaoLocalService
) {
    suspend fun coord2Address(latitude: Double, longitude: Double): KakaoCoordToAddressResponse =
        kakaoLocalService.coord2Address(longitude = longitude, latitude = latitude)
}
