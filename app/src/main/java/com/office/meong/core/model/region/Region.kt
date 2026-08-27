package com.office.meong.core.model.region

import kotlinx.serialization.Serializable

@Serializable
enum class Region(val label: String) {
    GANGNEUNG("강릉"),
    CHUNCHEON("춘천"),
    WONJU("원주"),

    /** 서버가 아직 앱이 모르는 지역을 내려줬을 때의 폴백. 응답 파싱이 통째로 깨지지 않도록 둔다. */
    UNKNOWN("알 수 없음");

    companion object {
        /** 사용자에게 선택지로 노출할 지역 목록(폴백값 제외). */
        val selectable: List<Region> = entries.filter { it != UNKNOWN }
    }
}
