package com.office.meong.core.model.place

enum class AcmpyType(val label: String) {
    INDOOR("실내만"),
    OUTDOOR("실외만"),
    INDOOR_OUTDOOR("전구역"),
    DESIGNATED("지정구역만"),
    UNKNOWN("정보없음");

    companion object {
        fun from(value: String?): AcmpyType = value?.let { v -> entries.firstOrNull { it.name == v } } ?: UNKNOWN
    }
}
