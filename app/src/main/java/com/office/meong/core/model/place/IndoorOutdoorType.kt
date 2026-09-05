package com.office.meong.core.model.place

enum class IndoorOutdoorType(val label: String) {
    IN("실내만"),
    OUT("실외만"),
    INOUT("전구역"),
    UNKNOWN("정보없음");

    companion object {
        fun from(value: String?): IndoorOutdoorType = value?.let { v -> entries.firstOrNull { it.name == v } } ?: UNKNOWN
    }
}
