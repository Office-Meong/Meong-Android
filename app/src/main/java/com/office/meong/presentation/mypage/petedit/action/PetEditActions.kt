package com.office.meong.presentation.mypage.petedit.action

import androidx.compose.runtime.Stable
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability

@Stable
interface PetEditActions {
    fun onImageClick()
    fun onNeuteredToggle(isNeutered: Boolean)
    fun onSizeSelect(size: PetSizeCategory)
    fun onActivitySelect(activity: PetActivityLevel)
    fun onSociabilitySelect(sociability: PetSociability)
    fun onHealthSelect(health: PetHealthStatus)
    fun onSaveClick()
}
