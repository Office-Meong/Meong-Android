package com.office.meong.domain.place.usecase

import com.office.meong.data.place.model.PlacePage
import com.office.meong.data.place.repository.PlaceRepository
import com.office.meong.domain.place.model.PlaceSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class PlaceSearchUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
) {
    /**
     * [queries] 가 방출될 때마다 장소를 조회한다.
     * - [debounce] 로 빠른 타이핑을 합치고, [mapLatest] 로 이전 조회는 취소한다.
     * - 같은 조건으로 다시 방출되면(재시도·당겨서 새로고침) 그대로 다시 조회한다.
     *   따라서 이 흐름에 중복 제거([distinctUntilChanged])를 넣지 않는다.
     */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun search(queries: Flow<PlaceSearchQuery>): Flow<Result<PlacePage>> =
        queries
            .debounce(DEBOUNCE_MILLIS.milliseconds)
            .mapLatest { query ->
                runCatching {
                    placeRepository.getPlaces(
                        region = query.region,
                        type = query.type,
                        keyword = query.keyword,
                        page = query.page,
                        size = query.size,
                    ).getOrThrow()
                }
            }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }
}
