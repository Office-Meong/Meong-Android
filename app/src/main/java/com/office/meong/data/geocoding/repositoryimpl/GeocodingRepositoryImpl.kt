package com.office.meong.data.geocoding.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.geocoding.remote.datasource.GeocodingDataSource
import com.office.meong.data.geocoding.repository.GeocodingRepository
import javax.inject.Inject

class GeocodingRepositoryImpl @Inject constructor(
    private val geocodingDataSource: GeocodingDataSource
) : GeocodingRepository {
    override suspend fun getAddress(latitude: Double, longitude: Double): Result<String?> = suspendRunCatching {
        val document = geocodingDataSource.coord2Address(latitude, longitude).documents.firstOrNull()
        document?.roadAddress?.addressName ?: document?.address?.addressName
    }
}
