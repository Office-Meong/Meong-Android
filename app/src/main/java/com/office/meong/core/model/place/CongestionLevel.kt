package com.office.meong.core.model.place

enum class CongestionLevel(val label: String) {
    RELAXED("여유"),
    NORMAL("보통"),
    CROWDED("붐빔"),
    VERY_CROWDED("매우붐빔"),
    UNKNOWN("정보없음");

    companion object {
        fun from(value: String): CongestionLevel = entries.firstOrNull { it.name == value } ?: UNKNOWN
    }
}
