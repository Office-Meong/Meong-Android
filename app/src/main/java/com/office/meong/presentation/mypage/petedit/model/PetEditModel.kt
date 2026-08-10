package com.office.meong.presentation.mypage.petedit.model

import com.office.meong.data.pet.model.PetInputModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

interface PetProfileAttribute {
    val label: String
}

enum class PetSize(override val label: String) : PetProfileAttribute {
    SMALL("소형견"), MEDIUM("중형견"), LARGE("대형견");

    companion object {
        val items: ImmutableList<PetSize> = entries.toPersistentList()
    }
}
enum class PetActivity(override val label: String) : PetProfileAttribute {
    LOW("낮음"), NORMAL("보통"), HIGH("높음");

    companion object {
        val items: ImmutableList<PetActivity> = entries.toPersistentList()
    }
}
enum class PetSociability(override val label: String) : PetProfileAttribute {
    FRIENDLY("친화적"), NORMAL("보통"), SENSITIVE("예민함");

    companion object {
        val items: ImmutableList<PetSociability> = entries.toPersistentList()
    }
}
enum class PetHealth(override val label: String) : PetProfileAttribute {
    HEALTHY("건강함"), HAS_DISEASE("지병 있음"), RECENT_SURGERY("최근 수술, 치료중");

    companion object {
        val items: ImmutableList<PetHealth> = entries.toPersistentList()
    }
}

fun PetEditUiState.toPetInputModel() = PetInputModel(
    name = petName,
    breed = breed,
    weightKg = weightKg.toDoubleOrNull() ?: 0.0,
    birthDate = birthDate,
    isNeutered = isNeutered,
    imageUrl = imageUrl.orEmpty(),
    sizeCategory = selectedSize?.name.orEmpty(),
    activityLevel = selectedActivity?.name.orEmpty(),
    sociability = selectedSociability?.name.orEmpty(),
    healthStatus = selectedHealth?.name.orEmpty(),
)
