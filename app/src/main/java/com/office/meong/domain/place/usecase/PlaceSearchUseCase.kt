package com.office.meong.domain.place.usecase

import android.util.LruCache
import com.office.meong.core.cache.getOrFetch
import com.office.meong.data.place.model.PlacePage
import com.office.meong.data.place.repository.PlaceRepository
import com.office.meong.domain.place.model.PlaceSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class PlaceSearchUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
) {
    private val cache = LruCache<PlaceSearchQuery, PlacePage>(CACHE_SIZE)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun search(queries: Flow<PlaceSearchQuery>): Flow<Result<PlacePage>> =
        queries
            .debounce(DEBOUNCE_MILLIS.milliseconds)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flowOf(
                    runCatching {
                        cache.getOrFetch(query) {
                            placeRepository.getPlaces(
                                region = query.region,
                                type = query.type,
                                keyword = query.keyword,
                                page = query.page,
                                size = query.size,
                            ).getOrThrow()
                        }
                    }
                )
            }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
        private const val CACHE_SIZE = 30
    }
}
