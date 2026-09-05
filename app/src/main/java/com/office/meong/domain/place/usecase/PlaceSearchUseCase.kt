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

class PlaceSearchUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
) {
    /**
     * [queries] 가 방출될 때마다 장소를 조회한다.
     * - 첫 페이지(검색어·필터 변경)만 [debounce] 로 합치고, 다음 페이지 요청은 즉시 보낸다.
     * - [mapLatest] 로 이전 조회는 취소한다.
     * - 같은 조건으로 다시 방출되면(재시도·당겨서 새로고침) 그대로 다시 조회한다.
     *   따라서 이 흐름에 중복 제거([distinctUntilChanged])를 넣지 않는다.
     * - [PlaceRepository.getPlaces] 는 내부에서 취소 예외를 다시 던지므로 여기서 별도로
     *   runCatching 으로 감싸지 않는다. 감싸면 [mapLatest] 가 이전 조회를 취소할 때 나오는
     *   취소 예외까지 실패로 삼켜, 정상 취소가 실패 스낵바로 이어진다.
     */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun search(queries: Flow<PlaceSearchQuery>): Flow<Result<PlacePage>> =
        queries
            .debounce { query -> if (query.page == 0) DEBOUNCE_MILLIS else 0L }
            .mapLatest { query ->
                placeRepository.getPlaces(
                    region = query.region,
                    type = query.type,
                    keyword = query.keyword,
                    page = query.page,
                    size = query.size,
                )
            }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }
}
