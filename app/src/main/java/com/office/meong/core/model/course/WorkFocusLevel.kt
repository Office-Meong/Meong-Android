package com.office.meong.core.model.course

enum class WorkFocusLevel(val label: String, val shortLabel: String) {
    LOW("업무는 최소한으로, 여행을 마음껏 즐길래요", "여유형"),
    MEDIUM("일과 여행, 적당히 균형을 맞출래요", "균형형"),
    HIGH("일에 몰입할 수 있는 환경이 필요해요", "집중형");

    companion object {
        fun from(value: String): WorkFocusLevel = entries.firstOrNull { it.name == value } ?: MEDIUM
    }
}
