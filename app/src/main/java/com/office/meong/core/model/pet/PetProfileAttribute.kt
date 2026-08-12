package com.office.meong.core.model.pet

interface PetProfileAttribute {
    val label: String
}

private inline fun <reified T> parseOrUnknown(value: String, unknown: T): T where T : Enum<T> =
    enumValues<T>().firstOrNull { it.name == value } ?: unknown

enum class PetSizeCategory(override val label: String) : PetProfileAttribute {
    SMALL("소형견"), MEDIUM("중형견"), LARGE("대형견"), UNKNOWN("알 수 없음");

    companion object {
        fun from(value: String): PetSizeCategory = parseOrUnknown(value, UNKNOWN)
    }
}

enum class PetActivityLevel(override val label: String) : PetProfileAttribute {
    LOW("낮음"), MEDIUM("보통"), HIGH("활발함"), UNKNOWN("알 수 없음");

    companion object {
        fun from(value: String): PetActivityLevel = parseOrUnknown(value, UNKNOWN)
    }
}

enum class PetSociability(override val label: String) : PetProfileAttribute {
    FRIENDLY("친화적"), NORMAL("보통"), SENSITIVE("예민함"), UNKNOWN("알 수 없음");

    companion object {
        fun from(value: String): PetSociability = parseOrUnknown(value, UNKNOWN)
    }
}

enum class PetHealthStatus(override val label: String) : PetProfileAttribute {
    HEALTHY("건강함"), HAS_CONDITION("지병 있음"), RECENT_TREATMENT("최근 수술 및 치료중"), UNKNOWN("알 수 없음");

    companion object {
        fun from(value: String): PetHealthStatus = parseOrUnknown(value, UNKNOWN)
    }
}
