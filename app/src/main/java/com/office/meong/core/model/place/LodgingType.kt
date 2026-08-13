package com.office.meong.core.model.place

enum class LodgingType(val label: String) {
    PENSION("펜션"),
    GUESTHOUSE("민박/게스트하우스/한옥"),
    CAMPING("캠핑장"),
    GLAMPING("글램핑장"),
    HOTEL("호텔/모텔/리조트"),
    CARAVAN("카라반");

    companion object {
        fun from(value: String?): LodgingType? = value?.let { v -> entries.firstOrNull { it.name == v } }
    }
}
