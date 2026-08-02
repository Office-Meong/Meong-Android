package com.office.meong.presentation.auth.model

enum class PetSize(val label: String) { SMALL("소형견"), MEDIUM("중형견"), LARGE("대형견") }
enum class PetActivity(val label: String) { LOW("낮음"), NORMAL("보통"), HIGH("높음") }
enum class PetSociability(val label: String) { FRIENDLY("친화적"), NORMAL("보통"), SENSITIVE("예민함") }
enum class PetHealth(val label: String) { HEALTHY("건강함"), HAS_DISEASE("지병 있음"), RECENT_SURGERY("최근 수술, 치료중") }
