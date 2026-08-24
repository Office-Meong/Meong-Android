package com.office.meong.data.geocoding.repository

interface GeocodingRepository {
    suspend fun getAddress(latitude: Double, longitude: Double): Result<String?>
}
